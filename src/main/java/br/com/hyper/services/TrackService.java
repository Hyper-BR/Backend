package br.com.hyper.services;

import br.com.hyper.dtos.requests.TrackRequestDTO;
import br.com.hyper.dtos.responses.pages.TrackPageResponseDTO;
import br.com.hyper.dtos.responses.TrackResponseDTO;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface TrackService {

    TrackResponseDTO save(TrackRequestDTO track, UUID artistId) throws IOException;

    TrackPageResponseDTO find(List<String> genres, Pageable pageable);

    TrackResponseDTO update(UUID id, TrackRequestDTO track);

    TrackResponseDTO findById(UUID id);

    void delete(UUID id);

    byte[] downloadTrack(UUID id);

    String getTrackUrl(UUID id);

}
