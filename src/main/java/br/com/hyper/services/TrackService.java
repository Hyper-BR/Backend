package br.com.hyper.services;

import br.com.hyper.dtos.PageResponseDTO;
import br.com.hyper.dtos.requests.TrackRequestDTO;
import br.com.hyper.dtos.responses.TrackResponseDTO;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface TrackService {

    PageResponseDTO<TrackResponseDTO> find(Pageable pageable);

    PageResponseDTO<TrackResponseDTO> findByArtistId(Pageable pageable, UUID customerId);

    TrackResponseDTO findById(UUID trackId);

    TrackResponseDTO update(UUID id, TrackRequestDTO track);

    Resource loadAudio(UUID id);

    void delete(UUID id);
}
