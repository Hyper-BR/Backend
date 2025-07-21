package br.com.hyper.controllers;

import br.com.hyper.dtos.requests.ReleaseRequestDTO;
import br.com.hyper.entities.CustomerEntity;
import br.com.hyper.services.ReleaseService;
import br.com.hyper.dtos.responses.ReleaseResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ReleaseController {

    private final ReleaseService releaseService;

    @PostMapping(value = "/release", consumes = { "multipart/form-data", "application/json" })
    public ResponseEntity<ReleaseResponseDTO> create(
            @ModelAttribute(value = "release") ReleaseRequestDTO release,
            @AuthenticationPrincipal CustomerEntity customer) {

        ReleaseResponseDTO response = releaseService.save(release, customer);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
