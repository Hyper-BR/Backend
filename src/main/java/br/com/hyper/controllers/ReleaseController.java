package br.com.hyper.controllers;

import br.com.hyper.dtos.PageResponseDTO;
import br.com.hyper.dtos.requests.ReleaseRequestDTO;
import br.com.hyper.entities.CustomerEntity;
import br.com.hyper.services.ReleaseService;
import br.com.hyper.dtos.responses.ReleaseResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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

    @GetMapping(value = "/release/artist/{artistId}")
    public ResponseEntity<PageResponseDTO<ReleaseResponseDTO>> get(@PathVariable("artistId") UUID artistId,
                                                                   @RequestParam(value = "page", defaultValue = "0", required = false) int page,
                                                                   @RequestParam(value = "size", defaultValue = "10", required = false) int size) {

        Pageable pageable = PageRequest.of(page, size);

        PageResponseDTO<ReleaseResponseDTO> response = releaseService.getReleasesByArtist(artistId, pageable);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping(value = "/release/customer")
    public ResponseEntity<PageResponseDTO<ReleaseResponseDTO>> get(@RequestParam(value = "page", defaultValue = "0", required = false) int page,
                                                                   @RequestParam(value = "size", defaultValue = "10", required = false) int size,
                                                                   @AuthenticationPrincipal CustomerEntity customer) {

        Pageable pageable = PageRequest.of(page, size);

        PageResponseDTO<ReleaseResponseDTO> response = releaseService.getReleasesByCustomer(customer.getId(), pageable);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
