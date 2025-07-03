package br.com.hyper.controllers;

import br.com.hyper.dtos.PageResponseDTO;
import br.com.hyper.dtos.requests.ArtistRequestDTO;
import br.com.hyper.dtos.responses.ArtistResponseDTO;
import br.com.hyper.entities.CustomerEntity;
import br.com.hyper.services.ArtistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ArtistController {

    private final ArtistService artistService;

    @PostMapping(value = "/artist")
    public ResponseEntity<ArtistResponseDTO> createArtist(@RequestBody @Valid ArtistRequestDTO artist,
                                                          @AuthenticationPrincipal CustomerEntity customer) {

        ArtistResponseDTO response = artistService.becomeArtist(artist, customer);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping(value = "/artist")
    public ResponseEntity<PageResponseDTO<ArtistResponseDTO>> find(
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "10", required = false) int size) {

        Pageable pageable = PageRequest.of(page, size);

        PageResponseDTO<ArtistResponseDTO> response = artistService.find(pageable);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping(value = "/artist/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id){

        artistService.delete(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
