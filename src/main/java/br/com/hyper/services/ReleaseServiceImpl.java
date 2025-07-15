package br.com.hyper.services;

import br.com.hyper.dtos.requests.ReleaseRequestDTO;
import br.com.hyper.dtos.requests.TrackRequestDTO;
import br.com.hyper.entities.CustomerEntity;
import br.com.hyper.entities.TrackEntity;
import br.com.hyper.enums.ReleaseStatus;
import br.com.hyper.enums.ReleaseType;
import br.com.hyper.exceptions.TrackException;
import br.com.hyper.constants.ErrorCodes;
import br.com.hyper.dtos.responses.ReleaseResponseDTO;
import br.com.hyper.entities.ArtistEntity;
import br.com.hyper.entities.ReleaseEntity;
import br.com.hyper.repositories.ArtistRepository;
import br.com.hyper.repositories.ReleaseRepository;
import br.com.hyper.repositories.TrackRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReleaseServiceImpl implements ReleaseService {

    private final TrackRepository trackRepository;
    private final ReleaseRepository releaseRepository;
    private final ArtistRepository artistRepository;
    private final ModelMapper modelMapper;

    @Override
    public ReleaseResponseDTO save(ReleaseRequestDTO releaseDTO, CustomerEntity customer) {

        UUID owner = null;
        try {
            if(Boolean.TRUE.equals(customer.getIsLabel())){
//                ArtistEntity label = findByIdOrThrowArtistDataNotFoundException(customer.getArtistProfile().getId());

            } else if (Boolean.TRUE.equals(customer.getIsArtist())) {
                ArtistEntity artist = findByIdOrThrowArtistDataNotFoundException(customer.getArtistProfile().getId());
                owner = artist.getId();
            } else {
                throw new TrackException(ErrorCodes.DATA_NOT_FOUND, "Usuário não pode subir arquivos.");
            }

            ReleaseEntity release = new ReleaseEntity();
            release.setUpc("1234567890123"); // TODO: Gerar UPC dinamicamente
            release.setReleaseDate(ZonedDateTime.now());
            release.setStatus(ReleaseStatus.DRAFT);
            release.setOwner(owner);
            release.setDescription(releaseDTO.getDescription());
            release.setTitle(releaseDTO.getTracks().getFirst().getTitle());

            Path releaseDir = createReleaseDirectory(release.getUpc());

            Path coverPath = processCoverFile(releaseDTO.getCover(), releaseDir);
            release.setImage(coverPath.toString());

            List<TrackEntity> savedTracks = processTracks(customer, releaseDTO.getTracks(), release, releaseDir, coverPath.toString());

            int totalDuration = savedTracks.stream().mapToInt(TrackEntity::getDurationInSeconds).sum();
            release.setTracks(savedTracks);
            release.setType(detectReleaseType(savedTracks.size(), totalDuration));

            releaseRepository.save(release);
            return modelMapper.map(release, ReleaseResponseDTO.class);

        } catch (IOException | CannotReadException | TagException |
                 InvalidAudioFrameException | ReadOnlyFileException e) {
            throw new TrackException(ErrorCodes.FILE_READ_ERROR, "Erro ao processar os arquivos.");
        }
    }

    private Path createReleaseDirectory(String upc) throws IOException {
        String basePath = System.getProperty("user.dir");
        Path dir = Paths.get(basePath, "uploads", "releases", upc);

        if (!Files.exists(dir)) {
            Files.createDirectories(dir);
            log.info("Diretório criado: {}", dir.toAbsolutePath());
        } else {
            log.info("Diretório já existe: {}", dir.toAbsolutePath());
        }

        return dir;
    }

    private Path processCoverFile(MultipartFile coverFile, Path dir) throws IOException {
        if (coverFile == null || coverFile.isEmpty()) {
            throw new TrackException(ErrorCodes.FILE_NOT_FOUND, "Capa não enviada.");
        }

        String coverExt = getExtension(coverFile.getOriginalFilename());
        String coverFileName = "cover." + coverExt;
        Path coverPath = dir.resolve(coverFileName);

        Files.createDirectories(dir);
        log.info("Salvando capa em: {}", coverPath.toAbsolutePath());
        coverFile.transferTo(coverPath.toFile());

        return coverPath;
    }


    private List<TrackEntity> processTracks(CustomerEntity customer,
                                            List<TrackRequestDTO> trackDTOs,
                                            ReleaseEntity release,
                                            Path dir,
                                            String coverUrl)
            throws IOException, CannotReadException, TagException,
            InvalidAudioFrameException, ReadOnlyFileException {

        List<TrackEntity> savedTracks = new ArrayList<>();

        for (TrackRequestDTO trackDTO : trackDTOs) {
            MultipartFile audioFile = trackDTO.getFile();
            if (audioFile == null || audioFile.isEmpty()) {
                throw new TrackException(ErrorCodes.FILE_NOT_FOUND, "Faixa não enviada.");
            }

            String trackExt = getExtension(audioFile.getOriginalFilename());
            String sanitizedTitle = sanitize(trackDTO.getTitle());
            Path trackPath = dir.resolve(sanitizedTitle + "." + trackExt);
            audioFile.transferTo(trackPath.toFile());

            AudioFile audio = AudioFileIO.read(trackPath.toFile());
            int duration = audio.getAudioHeader().getTrackLength();

            TrackEntity track = new TrackEntity();
            track.setTitle(trackDTO.getTitle());
            track.setDurationInSeconds(duration);
            track.setPrice(BigDecimal.valueOf(1.99));
            track.setGenre(trackDTO.getGenre());
            track.setTags(String.valueOf(trackDTO.getTags()));
            track.setRelease(release);
            track.setIsrc("BR-KRVO-23-" + UUID.randomUUID().toString().substring(0, 6));
            track.setFileUrl(trackPath.toString());
            track.setPlays(BigInteger.ZERO);
            track.setPrivacy(trackDTO.getPrivacy());
            track.setCoverUrl(coverUrl); 

            UUID ownerArtistId = customer.getArtistProfile().getId();
            ArtistEntity ownerArtist = findByIdOrThrowArtistDataNotFoundException(ownerArtistId);

            List<ArtistEntity> artists;
            if (trackDTO.getArtists() == null || trackDTO.getArtists().isEmpty()) {
                artists = List.of(ownerArtist);
            } else {
                artists = trackDTO.getArtists().stream()
                        .map(artistDTO -> findByIdOrThrowArtistDataNotFoundException(artistDTO.getId()))
                        .collect(Collectors.toList());

                if (artists.stream().noneMatch(a -> a.getId().equals(ownerArtistId))) {
                    artists.add(ownerArtist);
                }
            }

            track.setArtists(artists);
            savedTracks.add(track);
        }

        return savedTracks;
    }



    private String getExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) return "mp3";
        return fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
    }


    private ReleaseType detectReleaseType(int trackCount, int durationSeconds) {
        if (trackCount >= 7 || durationSeconds > 1800) {
            return ReleaseType.ALBUM;
        } else if (trackCount >= 3) {
            return ReleaseType.EP;
        } else {
            return ReleaseType.SINGLE;
        }
    }

    private String sanitize(String input) {
        return input.replaceAll("[^a-zA-Z0-9\\s]", "")
                .replaceAll("\\s+", "_")
                .toLowerCase();
    }

    private ArtistEntity findByIdOrThrowArtistDataNotFoundException(UUID id) {
        return artistRepository.findById(id)
                .orElseThrow(() -> new TrackException(ErrorCodes.DATA_NOT_FOUND, "Artist not found with ID: " + id));
    }
}

