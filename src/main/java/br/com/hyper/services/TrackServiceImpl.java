package br.com.hyper.services;

import br.com.hyper.constants.ErrorCodes;
import br.com.hyper.dtos.PageResponseDTO;
import br.com.hyper.dtos.requests.TrackRequestDTO;
import br.com.hyper.dtos.responses.TrackResponseDTO;
import br.com.hyper.entities.PlaylistEntity;
import br.com.hyper.entities.TrackEntity;
import br.com.hyper.exceptions.PlaylistNotFoundException;
import br.com.hyper.repositories.TrackRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrackServiceImpl implements TrackService {

    private final TrackRepository trackRepository;
    private final ModelMapper modelMapper;

    @Override
    public PageResponseDTO<TrackResponseDTO> find(Pageable pageable) {
        Page<TrackEntity> page = trackRepository.findAll(pageable);

        List<TrackResponseDTO> content = page.getContent().stream()
                .map(track -> modelMapper.map(track, TrackResponseDTO.class))
                .toList();

        return PageResponseDTO.<TrackResponseDTO>builder()
                .content(content)
                .page(page.getNumber())
                .size(page.getSize())
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .build();
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
        trackCurrent.setFileUrl(track.getFileUrl());
        trackCurrent.setExplicit(track.isExplicit());
        trackCurrent.setLanguage(track.getLanguage());

        TrackEntity trackEntity = trackRepository.save(trackCurrent);
        return modelMapper.map(trackEntity, TrackResponseDTO.class);
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
