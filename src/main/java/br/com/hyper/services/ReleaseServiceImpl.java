package br.com.hyper.services;

import br.com.hyper.dtos.requests.ReleaseRequestDTO;
import br.com.hyper.entities.CustomerEntity;
import br.com.hyper.entities.TrackEntity;
import br.com.hyper.enums.ReleaseStatus;
import br.com.hyper.enums.ReleaseType;
import br.com.hyper.exceptions.ReleaseNotFoundException;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;
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

    public ReleaseResponseDTO save(ReleaseRequestDTO releaseDTO, CustomerEntity customer) {
        File tempFile;
        try {
            if (releaseDTO.getFile() == null || releaseDTO.getFile().isEmpty()) {
                throw new TrackException(ErrorCodes.FILE_NOT_FOUND, "File is required for the release.");
            }

            ArtistEntity artist = findByIdOrThrowArtistDataNotFoundException(customer.getArtistProfile().getId());

            tempFile = File.createTempFile("track_", ".mp3");
            releaseDTO.getFile().transferTo(tempFile);

            AudioFile audioFile = AudioFileIO.read(tempFile);
            int durationInSeconds = audioFile.getAudioHeader().getTrackLength();

            ReleaseEntity release = new ReleaseEntity();
            release.setTitle(releaseDTO.getTitle());
            release.setType(releaseDTO.getType());
            release.setCoverUrl("https://example.com/cover.jpg");
            release.setUpc("1234567890123");
            release.setReleaseDate(ZonedDateTime.now());
            release.setStatus(ReleaseStatus.DRAFT);
            releaseRepository.save(release);

            String releaseDir = sanitize(releaseDTO.getTitle());
            String trackFileName = sanitize(releaseDTO.getTitle()) + ".mp3";
            Path outputPath = Paths.get("uploads", releaseDir, trackFileName);

            Files.createDirectories(outputPath.getParent());
            Files.copy(tempFile.toPath(), outputPath, StandardCopyOption.REPLACE_EXISTING);

            TrackEntity track = new TrackEntity();
            track.setTitle(releaseDTO.getTitle());
            track.setRelease(release);
            track.setDuration(durationInSeconds);
            track.setFileUrl(outputPath.toString());
            track.setPrice(BigDecimal.valueOf(1.99));
            track.setArtists(List.of(artist));
            track.setExplicit(false);
            track.setGenre(releaseDTO.getGenre());
            track.setIsrc("BR-KRVO-23-12345");
            track.setLanguage("Portuguese");

            trackRepository.save(track);
            tempFile.delete();

            release.setTracks(List.of(track));
            return modelMapper.map(release, ReleaseResponseDTO.class);

        } catch (IOException | CannotReadException | TagException | InvalidAudioFrameException | ReadOnlyFileException e) {
            throw new TrackException(ErrorCodes.FILE_READ_ERROR, e);
        }
    }
private String sanitize(String input) {
        return input.replaceAll("[^a-zA-Z0-9\\s]", "").replaceAll("\\s+", "_").toLowerCase();
    }

    private ArtistEntity findByIdOrThrowArtistDataNotFoundException(UUID id) {
        return artistRepository.findById(id).orElseThrow(
                () -> new TrackException(ErrorCodes.DATA_NOT_FOUND, "Artist not found with ID: " + id));
    }

}
