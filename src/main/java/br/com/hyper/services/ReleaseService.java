package br.com.hyper.services;

import br.com.hyper.dtos.PageResponseDTO;
import br.com.hyper.dtos.requests.ReleaseRequestDTO;
import br.com.hyper.dtos.responses.ReleaseResponseDTO;
import br.com.hyper.entities.CustomerEntity;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ReleaseService {

    ReleaseResponseDTO save(ReleaseRequestDTO track, CustomerEntity customer);

    PageResponseDTO<ReleaseResponseDTO> getReleasesByArtist(UUID artistId, Pageable pageable);

    PageResponseDTO<ReleaseResponseDTO> getReleasesByCustomer(UUID customerId, Pageable pageable);

}
