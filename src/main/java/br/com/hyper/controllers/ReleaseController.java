package br.com.hyper.controllers;

import br.com.hyper.dtos.requests.ReleaseRequestDTO;
import br.com.hyper.services.ReleaseService;
import br.com.hyper.dtos.responses.ReleaseResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ReleaseController {

    private final ReleaseService releaseService;

    @PostMapping(value = "/release", consumes = { "multipart/form-data" })
    public ResponseEntity<ReleaseResponseDTO> create(
            @RequestParam(value = "artistId") UUID artistId,
            @ModelAttribute(value = "release") ReleaseRequestDTO release) {

        ReleaseResponseDTO response = releaseService.save(release, artistId);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
