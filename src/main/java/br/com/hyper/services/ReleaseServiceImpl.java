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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZonedDateTime;
import java.util.*;

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
        try {
            ArtistEntity owner = findByIdOrThrowArtistDataNotFoundException(customer.getId());

            ReleaseEntity release = new ReleaseEntity();
            release.setUpc("1234567890123"); // TODO: gerar dinamicamente
            release.setReleaseDate(ZonedDateTime.now());
            release.setStatus(ReleaseStatus.DRAFT);
            release.setOwner(owner.getId());
            release.setDescription(releaseDTO.getDescription());

            Path dir = Paths.get("uploads", release.getUpc());
            Files.createDirectories(dir);

            MultipartFile coverFile = releaseDTO.getImage();
            if (coverFile == null || coverFile.isEmpty()) {
                throw new TrackException(ErrorCodes.FILE_NOT_FOUND, "Imagem da capa ausente.");
            }

            String coverExt = getExtension(coverFile.getOriginalFilename());
            String coverFileName = "cover." + coverExt;
            Path coverPath = dir.resolve(coverFileName);
            coverFile.transferTo(coverPath.toFile());

            String coverUrl = coverPath.toString();
            release.setImage(coverUrl);

            List<TrackEntity> savedTracks = new ArrayList<>();
            int totalDurationSeconds = 0;

            for (TrackRequestDTO trackDTO : releaseDTO.getTracks()) {
                MultipartFile audioFile = trackDTO.getFile();
                if (audioFile == null || audioFile.isEmpty()) {
                    throw new TrackException(ErrorCodes.FILE_NOT_FOUND, "Arquivo da faixa ausente.");
                }

                String trackExt = getExtension(audioFile.getOriginalFilename());
                String trackFileName = sanitize(trackDTO.getTitle()) + "." + trackExt;
                Path trackPath = dir.resolve(trackFileName);
                audioFile.transferTo(trackPath.toFile());

                File tempFile = trackPath.toFile();
                AudioFile audio = AudioFileIO.read(tempFile);
                int duration = audio.getAudioHeader().getTrackLength();
                totalDurationSeconds += duration;

                TrackEntity track = new TrackEntity();
                track.setTitle(trackDTO.getTitle());
                track.setDurationInSeconds(duration);
                track.setPrice(BigDecimal.valueOf(1.99));
                track.setGenre(trackDTO.getGenre());
                track.setTags(trackDTO.getTags());
                track.setRelease(release);
                track.setIsrc("BR-KRVO-23-" + UUID.randomUUID().toString().substring(0, 6));
                track.setFileUrl(trackPath.toString());

                List<ArtistEntity> artists = trackDTO.getArtists().stream()
                        .map(artistDTO -> findByIdOrThrowArtistDataNotFoundException(artistDTO.getId()))
                        .toList();

                track.setArtists(artists);

                trackRepository.save(track);
                savedTracks.add(track);
            }

            ReleaseType releaseType = detectReleaseType(savedTracks.size(), totalDurationSeconds);
            release.setTracks(savedTracks);
            release.setType(releaseType);

            releaseRepository.save(release);

            return modelMapper.map(release, ReleaseResponseDTO.class);

        } catch (IOException | CannotReadException | TagException |
                 InvalidAudioFrameException | ReadOnlyFileException e) {
            throw new TrackException(ErrorCodes.FILE_READ_ERROR, "Erro ao processar release");
        }
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

