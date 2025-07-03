package br.com.hyper.services;

import br.com.hyper.dtos.responses.pages.TrackPageResponseDTO;
import org.springframework.data.domain.Pageable;

public interface TrackService {

    TrackPageResponseDTO find(Pageable pageable);

}
