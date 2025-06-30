package br.com.hyper.services;

import br.com.hyper.dtos.requests.TrackRequestDTO;
import br.com.hyper.dtos.responses.pages.TrackPageResponseDTO;
import br.com.hyper.dtos.responses.TrackResponseDTO;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;

public interface TrackService {

    TrackResponseDTO save(TrackRequestDTO track, Long artistId) throws IOException;

    TrackPageResponseDTO find(List<String> genres, Pageable pageable);

    TrackResponseDTO update(Long id, TrackRequestDTO track);

    TrackResponseDTO findById(Long id);

    void delete(Long id);

    byte[] downloadTrack(Long id);

    String getTrackUrl(Long id);

}
