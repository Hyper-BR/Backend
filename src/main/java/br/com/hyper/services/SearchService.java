package br.com.hyper.services;

import br.com.hyper.dtos.PageResponseDTO;
import br.com.hyper.dtos.responses.ArtistResponseDTO;
import br.com.hyper.dtos.responses.TrackResponseDTO;
import org.springframework.data.domain.Pageable;

public interface SearchService {

    PageResponseDTO<ArtistResponseDTO> searchArtists(String query, Pageable pageable);
    PageResponseDTO<TrackResponseDTO> searchTracks(String query, Pageable pageable);

}
