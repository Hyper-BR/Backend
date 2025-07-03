package br.com.hyper.services;

import br.com.hyper.dtos.PageResponseDTO;
import br.com.hyper.dtos.requests.TrackRequestDTO;
import br.com.hyper.dtos.responses.TrackResponseDTO;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface TrackService {

    PageResponseDTO<TrackResponseDTO> find(Pageable pageable);
    TrackResponseDTO update(UUID id, TrackRequestDTO track);

    void delete(UUID id);
}
