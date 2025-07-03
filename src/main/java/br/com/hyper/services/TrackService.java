package br.com.hyper.services;

import br.com.hyper.dtos.PageResponseDTO;
import br.com.hyper.dtos.responses.TrackResponseDTO;
import org.springframework.data.domain.Pageable;

public interface TrackService {

    PageResponseDTO<TrackResponseDTO> find(Pageable pageable);

}
