package br.com.hyper.services;

import br.com.hyper.dtos.requests.ReleaseRequestDTO;
import br.com.hyper.dtos.responses.ReleaseResponseDTO;
import br.com.hyper.entities.CustomerEntity;

public interface ReleaseService {

    ReleaseResponseDTO save(ReleaseRequestDTO track, CustomerEntity customer);

}
