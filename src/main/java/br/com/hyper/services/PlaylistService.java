package br.com.hyper.services;

import br.com.hyper.dtos.responses.pages.PlaylistPageReponseDTO;
import br.com.hyper.dtos.responses.PlaylistResponseDTO;
import br.com.hyper.dtos.requests.PlaylistRequestDTO;
import br.com.hyper.entities.CustomerEntity;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PlaylistService {

    PlaylistResponseDTO save(PlaylistRequestDTO playlist, CustomerEntity customerId);

    PlaylistPageReponseDTO find(String name, Pageable pageable);

    List<PlaylistResponseDTO> findByCustomer(Long id);

    PlaylistResponseDTO update(Long id, PlaylistRequestDTO playlist);

    void delete(Long id);

    PlaylistResponseDTO updateName(Long id, String name);

    PlaylistResponseDTO addTrack(Long id, Long trackId);
}
