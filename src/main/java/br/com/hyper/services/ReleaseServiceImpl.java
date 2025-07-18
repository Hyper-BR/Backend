package br.com.hyper.services;

import br.com.hyper.constants.DefaultAssets;
import br.com.hyper.dtos.requests.ReleaseRequestDTO;
import br.com.hyper.dtos.requests.TrackRequestDTO;
import br.com.hyper.entities.*;
import br.com.hyper.enums.Privacy;
import br.com.hyper.enums.ReleaseStatus;
import br.com.hyper.enums.ReleaseType;
import br.com.hyper.constants.ErrorCodes;
import br.com.hyper.dtos.responses.ReleaseResponseDTO;
import br.com.hyper.exceptions.GenericException;
import br.com.hyper.repositories.ArtistRepository;
import br.com.hyper.repositories.LabelRepository;
import br.com.hyper.repositories.ReleaseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

    private final ReleaseRepository releaseRepository;
    private final ArtistRepository artistRepository;
    private final LabelRepository labelRepository;
    private final ModelMapper modelMapper;

    @Override
    public ReleaseResponseDTO save(ReleaseRequestDTO releaseDTO, CustomerEntity customer) {

        UUID owner;
        ArtistEntity artist = null;
        LabelEntity label = null;
        try {
            if(Boolean.TRUE.equals(customer.getIsLabel())){
                label = findByIdOrThrowLabelDataNotFoundException(customer.getId());
                owner = label.getId();
            } else if (Boolean.TRUE.equals(customer.getIsArtist())) {
                artist = findByIdOrThrowArtistDataNotFoundException(customer.getArtistProfile().getId());
                owner = artist.getId();
            } else {
                throw new GenericException(ErrorCodes.DATA_NOT_FOUND, ErrorCodes.DATA_NOT_FOUND.getMessage());
            }

            ReleaseEntity release = createRelease(releaseDTO, owner);

            // TODO: Salvar em bucket

            Path releaseDir = createReleaseDirectory(release.getUpc());

            if(releaseDTO.getCover() == null || releaseDTO.getCover().isEmpty() ) {
                release.setCoverUrl(customer.getAvatarUrl());
            } else {
                String coverRelativePath = processCoverFile(releaseDTO.getCover(), releaseDir);
                release.setCoverUrl("/" + coverRelativePath);
            }

            List<TrackEntity> savedTracks;
            if(Boolean.TRUE.equals(customer.getIsLabel())){
                savedTracks = processArtistTracks(artist, releaseDTO.getTracks(), release, releaseDir); // TODO
            } else if (Boolean.TRUE.equals(customer.getIsArtist())) {
                savedTracks = processArtistTracks(artist, releaseDTO.getTracks(), release, releaseDir);
            } else {
                throw new GenericException(ErrorCodes.DATA_NOT_FOUND, ErrorCodes.DATA_NOT_FOUND.getMessage());
            }

            release.setTracks(savedTracks);
            if(savedTracks.size() > 1) {
                release.setType(releaseDTO.getType());
            } else {
                release.setType(ReleaseType.SINGLE);
            }

            releaseRepository.save(release);

            customer.getArtistProfile().setFreeTrackLimit(
                    customer.getArtistProfile().getFreeTrackLimit() - savedTracks.size()
            );

            return modelMapper.map(release, ReleaseResponseDTO.class);

        } catch (IOException | CannotReadException | TagException |
                 InvalidAudioFrameException | ReadOnlyFileException e) {
            throw new GenericException(ErrorCodes.FILE_READ_ERROR, ErrorCodes.FILE_READ_ERROR.getMessage());
        }
    }

    private ReleaseEntity createRelease(ReleaseRequestDTO releaseDTO, UUID owner) {
        ReleaseEntity release = new ReleaseEntity();
        release.setUpc("TODO_" + UUID.randomUUID().toString().substring(0, 9).replace("-", ""));
        release.setReleaseDate(ZonedDateTime.now());
        release.setStatus(ReleaseStatus.DRAFT);
        release.setOwner(owner);
        release.setDescription(releaseDTO.getDescription());
        release.setTitle(releaseDTO.getTracks().getFirst().getTitle());

        return release;
    }

    private Path createReleaseDirectory(String upc) throws IOException {
        String basePath = System.getProperty("user.dir");
        Path dir = Paths.get(basePath, "uploads", "releases", upc);

        if (!Files.exists(dir)) {
            Files.createDirectories(dir);
        }

        return dir;
    }

    private String processCoverFile(MultipartFile coverFile, Path dir) throws IOException {
        String coverExt = getExtension(coverFile.getOriginalFilename());
        String coverFileName = "cover." + coverExt;
        Path coverPath = dir.resolve(coverFileName);

        Files.createDirectories(dir);
        coverFile.transferTo(coverPath.toFile());

        Path relativePath = Paths.get("uploads", "releases", dir.getFileName().toString(), coverFileName);
        return relativePath.toString().replace("\\", "/");
    }

    private List<TrackEntity> processArtistTracks(ArtistEntity artist, List<TrackRequestDTO> trackDTOs, ReleaseEntity release, Path dir)
            throws IOException, CannotReadException, TagException,
            InvalidAudioFrameException, ReadOnlyFileException {

        List<TrackEntity> savedTracks = new ArrayList<>();

        int existingPublicTracks = artistRepository.countPublicTracks(artist.getId(), Privacy.PUBLIC);
        int remainingPublicSlots = 5 - existingPublicTracks;

        for (TrackRequestDTO trackDTO : trackDTOs) {
            MultipartFile audioFile = trackDTO.getFile();
            if (audioFile == null || audioFile.isEmpty()) {
                throw new GenericException(ErrorCodes.FILE_NOT_FOUND, ErrorCodes.FILE_NOT_FOUND.getMessage());
            }

            String trackExt = getExtension(audioFile.getOriginalFilename());
            String sanitizedTitle = sanitize(trackDTO.getTitle());
            Path trackPath = dir.resolve(sanitizedTitle + "." + trackExt);
            audioFile.transferTo(trackPath.toFile());

            AudioFile audio = AudioFileIO.read(trackPath.toFile());
            int duration = audio.getAudioHeader().getTrackLength();

            TrackEntity track = new TrackEntity();
            if(trackDTO.getTitle() == null || trackDTO.getTitle().isEmpty()) {
                throw new GenericException(ErrorCodes.DATA_NOT_FOUND, "Track title is required.");
            }

            track.setTitle(trackDTO.getTitle());
            track.setDuration(duration);

            track.setPrice(BigDecimal.valueOf(1.99));
            track.setRelease(release);
            track.setIsrc("BR-KRVO-23-" + UUID.randomUUID().toString().substring(0, 6) + "TODO");
            track.setFileUrl(trackPath.toString());
            track.setPlays(BigInteger.ZERO);
            track.setBpm("180"); // TODO: Extract BPM from audio
            track.setKey("7A"); // TODO: Extract key from audio

            if (trackDTO.getPrivacy() == Privacy.PUBLIC && remainingPublicSlots > 0) {
                track.setPrivacy(Privacy.PUBLIC);
                remainingPublicSlots--;
            } else {
                track.setPrivacy(Privacy.PRIVATE);
            }

            List<ArtistEntity> artists;
            if (trackDTO.getArtists() == null || trackDTO.getArtists().isEmpty()) {
                artists = List.of(artist);
            } else {
                artists = trackDTO.getArtists().stream()
                        .map(artistDTO -> findByIdOrThrowArtistDataNotFoundException(artistDTO.getId()))
                        .collect(Collectors.toList());

                if (artists.stream().noneMatch(a -> a.getId().equals(artist.getId()))) {
                    artists.add(artist);
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

    private String sanitize(String input) {
        return input.replaceAll("[^a-zA-Z0-9\\s]", "")
                .replaceAll("\\s+", "_")
                .toLowerCase();
    }

    private ArtistEntity findByIdOrThrowArtistDataNotFoundException(UUID id) {
        return artistRepository.findById(id)
                .orElseThrow(() -> new GenericException(ErrorCodes.DATA_NOT_FOUND, "Artist not found with ID: " + id));
    }

    private LabelEntity findByIdOrThrowLabelDataNotFoundException(UUID id) {
        return labelRepository.findById(id)
                .orElseThrow(() -> new GenericException(ErrorCodes.DATA_NOT_FOUND, "Label not found with ID: " + id));
    }
}

