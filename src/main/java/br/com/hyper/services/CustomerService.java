package br.com.hyper.services;

import br.com.hyper.dtos.PageResponseDTO;
import br.com.hyper.dtos.requests.CustomerRequestDTO;
import br.com.hyper.dtos.responses.CustomerResponseDTO;
import br.com.hyper.entities.CustomerEntity;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface CustomerService {

    CustomerResponseDTO findByEmail(String email);

    PageResponseDTO<CustomerResponseDTO> findAll(Pageable pageable);

    CustomerResponseDTO update(UUID id, CustomerRequestDTO user, CustomerEntity customer);

    void delete(UUID id);
}
