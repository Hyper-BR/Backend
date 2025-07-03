package br.com.hyper.services;

import br.com.hyper.dtos.responses.pages.PlaylistPageReponseDTO;
import br.com.hyper.dtos.responses.PlaylistResponseDTO;
import br.com.hyper.dtos.requests.PlaylistRequestDTO;
import br.com.hyper.entities.CustomerEntity;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface PlaylistService {

    PlaylistResponseDTO save(PlaylistRequestDTO playlist, CustomerEntity customerId);

    PlaylistPageReponseDTO find(String name, Pageable pageable);

    List<PlaylistResponseDTO> findByCustomer(UUID id);

    PlaylistResponseDTO update(UUID id, PlaylistRequestDTO playlist);

    void delete(UUID id);

    PlaylistResponseDTO updateName(UUID id, String name);

    PlaylistResponseDTO addTrack(UUID id, UUID trackId);
}
