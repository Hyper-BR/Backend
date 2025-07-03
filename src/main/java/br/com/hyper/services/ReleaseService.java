package br.com.hyper.services;

import br.com.hyper.dtos.requests.ReleaseRequestDTO;
import br.com.hyper.dtos.responses.ReleaseResponseDTO;

import java.util.UUID;

public interface ReleaseService {

    ReleaseResponseDTO save(ReleaseRequestDTO track, UUID artistId);

}
