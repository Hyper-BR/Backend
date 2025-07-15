package br.com.hyper.services;

import br.com.hyper.constants.ErrorCodes;
import br.com.hyper.dtos.PageResponseDTO;
import br.com.hyper.dtos.requests.TrackRequestDTO;
import br.com.hyper.dtos.responses.TrackResponseDTO;
import br.com.hyper.entities.TrackEntity;
import br.com.hyper.exceptions.PlaylistNotFoundException;
import br.com.hyper.repositories.TrackRepository;
import br.com.hyper.utils.PaginationMapper;
import org.springframework.core.io.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
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
        Page<TrackEntity> page = trackRepository.findAll(pageable);

        return paginationMapper.map(page, TrackResponseDTO.class);
    }

    @Override
    public PageResponseDTO<TrackResponseDTO> findByArtistId(Pageable pageable, UUID customerId) {
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
            throw new PlaylistNotFoundException(ErrorCodes.INVALID_DATA, "Track data cannot be null or empty");
        }

        if (track.getTitle().length() > 50) {
            log.error("Track name exceeds maximum length of 50 characters");
            throw new PlaylistNotFoundException(ErrorCodes.INVALID_DATA, "Track name exceeds maximum length of 50 characters");
        }

        TrackEntity trackCurrent = findByIdOrThrowTrackDataNotFoundException(id);

        trackCurrent.setTitle(track.getTitle());
        trackCurrent.setGenre(track.getGenre());

        TrackEntity trackEntity = trackRepository.save(trackCurrent);
        return modelMapper.map(trackEntity, TrackResponseDTO.class);
    }

    @Override
    public Resource loadAudio(UUID id) {
        TrackEntity track = findByIdOrThrowTrackDataNotFoundException(id);
        String filePath = track.getRelease().getCoverUrl();

        try {
            Path path = Paths.get(filePath);
            Resource resource = new UrlResource(path.toUri());

            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Arquivo não encontrado ou não legível");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("URL do arquivo malformada", e);
        }
    }

    @Override
    public void delete(UUID id) {
        TrackEntity track = findByIdOrThrowTrackDataNotFoundException(id);

        trackRepository.delete(track);
    }

    private TrackEntity findByIdOrThrowTrackDataNotFoundException(UUID id) {
        return trackRepository.findById(id)
                .orElseThrow(() -> new PlaylistNotFoundException(ErrorCodes.DATA_NOT_FOUND, "Track not found with id: " + id));
    }

}
