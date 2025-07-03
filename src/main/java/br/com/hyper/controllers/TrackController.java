package br.com.hyper.controllers;

import br.com.hyper.dtos.PageResponseDTO;
import br.com.hyper.dtos.requests.PlaylistRequestDTO;
import br.com.hyper.dtos.requests.TrackRequestDTO;
import br.com.hyper.dtos.responses.PlaylistResponseDTO;
import br.com.hyper.dtos.responses.TrackResponseDTO;
import br.com.hyper.services.TrackService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@Slf4j
@RequiredArgsConstructor
public class TrackController {

    private final TrackService trackService;

    @GetMapping(value = "/tracks")
    public ResponseEntity<PageResponseDTO<TrackResponseDTO>> find(
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "10", required = false) int size) {

        Pageable pageable = PageRequest.of(page, size);

        PageResponseDTO<TrackResponseDTO> response = trackService.find(pageable);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping(value = "/tracks/{id}")
    public ResponseEntity<TrackResponseDTO> update(@PathVariable UUID id, @RequestBody @Valid TrackRequestDTO track) {

        TrackResponseDTO response = trackService.update(id, track);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping(value = "/tracks/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id){

        trackService.delete(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
