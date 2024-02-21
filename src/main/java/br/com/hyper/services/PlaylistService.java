package br.com.hyper.services;

import br.com.hyper.dtos.responses.pages.PlaylistPageReponseDTO;
import br.com.hyper.dtos.responses.PlaylistResponseDTO;
import br.com.hyper.dtos.requests.PlaylistRequestDTO;
import org.springframework.data.domain.Pageable;

public interface PlaylistService {

    PlaylistResponseDTO save(PlaylistRequestDTO playlist);

    PlaylistPageReponseDTO find(String name, Pageable pageable);

    PlaylistResponseDTO updateName(Long id, String name);

    PlaylistResponseDTO addTrack(Long id, Long trackId);

    void delete(Long id);

}
