package br.com.hyper.services;

import br.com.hyper.dtos.PageResponseDTO;
import br.com.hyper.dtos.responses.PlaylistResponseDTO;
import br.com.hyper.dtos.requests.PlaylistRequestDTO;
import br.com.hyper.entities.CustomerEntity;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface PlaylistService {

    PlaylistResponseDTO save(PlaylistRequestDTO playlist, CustomerEntity customerId);

    PageResponseDTO<PlaylistResponseDTO> find(Pageable pageable);

    PlaylistResponseDTO findById(UUID id);

    List<PlaylistResponseDTO> findByCustomer(UUID id);

    PlaylistResponseDTO update(UUID id, PlaylistRequestDTO playlist);

    void delete(UUID id);

    PlaylistResponseDTO addTrack(UUID id, UUID trackId);

    PlaylistResponseDTO removeTrack(UUID id, UUID trackId);
}
