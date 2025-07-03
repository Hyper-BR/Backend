package br.com.hyper.services;

import br.com.hyper.dtos.requests.ArtistRequestDTO;
import br.com.hyper.dtos.responses.ArtistResponseDTO;
import br.com.hyper.dtos.responses.pages.ArtistPageResponseDTO;
import br.com.hyper.entities.CustomerEntity;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface ArtistService {

    ArtistResponseDTO save(ArtistRequestDTO artist, CustomerEntity customer);

    ArtistPageResponseDTO find(List<String> names, Pageable pageable);

    void delete(UUID id);
}
