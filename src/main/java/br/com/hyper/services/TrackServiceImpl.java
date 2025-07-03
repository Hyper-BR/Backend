package br.com.hyper.services;

import br.com.hyper.dtos.responses.pages.TrackPageResponseDTO;
import br.com.hyper.entities.TrackEntity;
import br.com.hyper.exceptions.TrackException;
import br.com.hyper.constants.ErrorCodes;
import br.com.hyper.dtos.responses.TrackResponseDTO;
import br.com.hyper.entities.ArtistEntity;
import br.com.hyper.dtos.requests.TrackRequestDTO;
import br.com.hyper.entities.ReleaseEntity;
import br.com.hyper.repositories.ArtistRepository;
import br.com.hyper.repositories.TrackRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrackServiceImpl implements TrackService {

    @Autowired
    private final TrackRepository trackRepository;

    @Autowired
    private final ArtistRepository artistRepository;

    @Autowired
    private final ModelMapper modelMapper;

    @Override
    public TrackResponseDTO save(TrackRequestDTO track, UUID artistId) throws IOException {

        ArtistEntity artist = findByIdOrThrowArtistDataNotFoundException(artistId);

        TrackEntity trackEntity = TrackEntity.builder()
                .title(track.getName())
                .price(3)
                .genre(track.getGenre())
                .release(new ReleaseEntity())
                .fileUrl(artist.getName() + "/" + track.getGenre() + "/" + track.getName())
                .build();

        // Salva a track localmente - remover

        try {
            Path outputPath = Paths.get("uploads", trackEntity.getTitle() + ".mp3");
            Files.createDirectories(outputPath.getParent());
            try (InputStream inputStream = track.getFile().getInputStream()) {
                Files.copy(inputStream, outputPath, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            throw new TrackException(ErrorCodes.FILE_READ_ERROR, e);
        }

        // Salva a track no S3
//        amazonBucketS3.uploadArtistTrack(trackEntity.getPath(), track.getFile());


        return modelMapper.map(trackEntity, TrackResponseDTO.class);

    }

    @Override
    public TrackPageResponseDTO find(List<String> genres, Pageable pageable) {

        Page<TrackEntity> trackEntities;

        if(genres != null){
            trackEntities = trackRepository.findByGenres(genres, pageable);
        } else {
            trackEntities = trackRepository.findAll(pageable);
        }

        return modelMapper.map(trackEntities, TrackPageResponseDTO.class);
    }

    @Override
    public TrackResponseDTO findById(UUID id) {

        TrackEntity track = findByIdOrThrowTrackDataNotFoundException(id);

        return modelMapper.map(track, TrackResponseDTO.class);
    }


    @Override
    public TrackResponseDTO update(UUID id, TrackRequestDTO track) {
        TrackEntity trackCurrent = findByIdOrThrowTrackDataNotFoundException(id);

//        trackCurrent.setName(track.getName());
//        trackCurrent.setGenre(track.getGenre());
//        trackCurrent.setImage(track.getImage());
//        trackCurrent.setSize(track.getFile().getSize() / 1024f);
//        trackCurrent.setPath(trackCurrent.getArtist().getName() + "/" + track.getGenre() + "/" + track.getName());

        trackRepository.save(trackCurrent);

        return modelMapper.map(trackCurrent, TrackResponseDTO.class);
    }

    @Override
    public void delete(UUID id) {
        TrackEntity musicCurrent = findByIdOrThrowTrackDataNotFoundException(id);

        trackRepository.delete(musicCurrent);
    }


    public byte[] downloadTrack(UUID id) {

        TrackEntity track = findByIdOrThrowTrackDataNotFoundException(id);
//        return amazonBucketS3.downloadTrack(track.getPath());

        Path filePath = Paths.get("uploads", track.getTitle() + ".mp3");
        try {
            return Files.readAllBytes(filePath);
        } catch (IOException e) {
            throw new TrackException(ErrorCodes.FILE_READ_ERROR, e);
        }
    }

    public String getTrackUrl(UUID id) {

        TrackEntity track = findByIdOrThrowTrackDataNotFoundException(id);

//        return amazonBucketS3.getTrackUrl(track.getPath());
        return Paths.get("uploads", track.getTitle() + ".mp3").toString();
    }

    private TrackEntity findByIdOrThrowTrackDataNotFoundException(UUID id) {
        return trackRepository.findById(id).orElseThrow(
                () -> new TrackException(ErrorCodes.DATA_NOT_FOUND, ErrorCodes.DATA_NOT_FOUND.getMessage()));
    }

    private ArtistEntity findByIdOrThrowArtistDataNotFoundException(UUID id) {
        return artistRepository.findById(id).orElseThrow(
                () -> new TrackException(ErrorCodes.DATA_NOT_FOUND, ErrorCodes.DATA_NOT_FOUND.getMessage()));
    }

}
