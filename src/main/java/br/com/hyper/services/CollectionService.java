package br.com.hyper.services;

import br.com.hyper.dtos.requests.CollectionRequestDTO;
import br.com.hyper.dtos.responses.CollectionResponseDTO;
import br.com.hyper.dtos.responses.pages.CollectionPageResponseDTO;
import org.springframework.data.domain.Pageable;

public interface CollectionService {

    public CollectionResponseDTO save(CollectionRequestDTO collection);

    public CollectionPageResponseDTO find(String collection, Pageable pageable);

    public CollectionResponseDTO update(Long id, CollectionRequestDTO collection);
    public void delete(Long id);

}