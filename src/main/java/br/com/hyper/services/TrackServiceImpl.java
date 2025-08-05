package br.com.hyper.services;

import br.com.hyper.enums.ErrorCodes;
import br.com.hyper.dtos.PageResponseDTO;
import br.com.hyper.dtos.requests.TrackRequestDTO;
import br.com.hyper.dtos.responses.TrackResponseDTO;
import br.com.hyper.entities.TrackEntity;
import br.com.hyper.enums.Privacy;
import br.com.hyper.exceptions.GenericException;
import br.com.hyper.repositories.TrackRepository;
import br.com.hyper.utils.LocalFileStorageUtil;
import br.com.hyper.utils.PaginationMapper;
import org.springframework.core.io.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrackServiceImpl implements TrackService {

    private final TrackRepository trackRepository;
    private final ModelMapper modelMapper;
    private final PaginationMapper paginationMapper;

    @Override
    public PageResponseDTO<TrackResponseDTO> find(Pageable pageable) {
        Page<TrackEntity> page = trackRepository.findAllTracksByPrivacy(Privacy.PUBLIC, pageable);

        return paginationMapper.map(page, TrackResponseDTO.class);
    }

    @Override
    public PageResponseDTO<TrackResponseDTO> findByArtistId(Pageable pageable, UUID artistId) {
        Page<TrackEntity> page = trackRepository.findByArtistIdAndPrivacy(artistId, Privacy.PUBLIC, pageable);

        return paginationMapper.map(page,TrackResponseDTO.class);
    }

    @Override
    public PageResponseDTO<TrackResponseDTO> findAllByCustomer(Pageable pageable, UUID customerId) {

        Page<TrackEntity> page = trackRepository.findByArtistId(customerId, pageable);

        return paginationMapper.map(page,TrackResponseDTO.class);
    }

    @Override
    public TrackResponseDTO findById(UUID trackId) {
        TrackEntity track = findByIdOrThrowTrackDataNotFoundException(trackId);

        return modelMapper.map(track, TrackResponseDTO.class);
    }

    @Override
    public TrackResponseDTO update(UUID id, TrackRequestDTO track) {

        if (track == null || track.getTitle() == null || track.getTitle().isEmpty()) {
            log.error("Track data is invalid");
            throw new GenericException(ErrorCodes.INVALID_DATA, "Track data cannot be null or empty");
        }

        if (track.getTitle().length() > 50) {
            log.error("Track name exceeds maximum length of 50 characters");
            throw new GenericException(ErrorCodes.INVALID_DATA, "Track name exceeds maximum length of 50 characters");
        }

        TrackEntity trackCurrent = findByIdOrThrowTrackDataNotFoundException(id);

        trackCurrent.setTitle(track.getTitle());

        TrackEntity trackEntity = trackRepository.save(trackCurrent);
        return modelMapper.map(trackEntity, TrackResponseDTO.class);
    }

    @Override
    public Resource loadAudio(UUID id) {
        TrackEntity track = findByIdOrThrowTrackDataNotFoundException(id);

        try {
            return new UrlResource(LocalFileStorageUtil.searchFile(track.getFileUrl()).getURL());
        } catch (IOException e) {
            throw new GenericException(ErrorCodes.INVALID_DATA, e.getMessage());
        }
    }


    @Override
    public void delete(UUID id) {
        TrackEntity track = findByIdOrThrowTrackDataNotFoundException(id);

        trackRepository.delete(track);

        try {
            String filePath = track.getFileUrl();
            Path path = Path.of(filePath);

            if (Files.exists(path)) {
                Files.delete(path);
            }
        } catch (IOException e) {
            log.warn("Erro ao remover arquivo físico do track {}: {}", id, e.getMessage());
            throw new RuntimeException(e);
        }
    }


    private TrackEntity findByIdOrThrowTrackDataNotFoundException(UUID id) {
        return trackRepository.findById(id)
                .orElseThrow(() -> new GenericException(ErrorCodes.DATA_NOT_FOUND, ErrorCodes.DATA_NOT_FOUND.getMessage()));
    }

}
