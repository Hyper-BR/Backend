package br.com.hyper.controllers;

import br.com.hyper.dtos.PageResponseDTO;
import br.com.hyper.dtos.requests.PlaylistRequestDTO;
import br.com.hyper.dtos.responses.PlaylistResponseDTO;
import br.com.hyper.entities.CustomerEntity;
import br.com.hyper.services.PlaylistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@Slf4j
@RequiredArgsConstructor
public class PlaylistController {

    private final PlaylistService playlistService;

    @PostMapping(value = "/playlists")
    public ResponseEntity<PlaylistResponseDTO> save(
            @RequestBody PlaylistRequestDTO playlist, @AuthenticationPrincipal CustomerEntity customer) {

        PlaylistResponseDTO response = playlistService.save(playlist, customer);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping(value = "/playlists")
    public ResponseEntity<PageResponseDTO<PlaylistResponseDTO>> find(
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "10", required = false) int size) {

        Pageable pageable = PageRequest.of(page, size);

        PageResponseDTO<PlaylistResponseDTO> response = playlistService.find(pageable);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(value = "/playlists/customer")
    public ResponseEntity<List<PlaylistResponseDTO>> findByCustomer(@AuthenticationPrincipal CustomerEntity customer) {

        List<PlaylistResponseDTO> response = playlistService.findByCustomer(customer.getId());

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(value = "/playlists/{playlistId}")
    public ResponseEntity<PlaylistResponseDTO> findById(@PathVariable UUID playlistId) {

        PlaylistResponseDTO response = playlistService.findById(playlistId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping(value = "/playlists/{id}")
    public ResponseEntity<PlaylistResponseDTO> update(@PathVariable UUID id, @RequestBody @Valid PlaylistRequestDTO playlist) {

        PlaylistResponseDTO response = playlistService.update(id, playlist);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping(value = "/playlists/{playlistId}/tracks/{trackId}")
    public ResponseEntity<PlaylistResponseDTO> addTrack(@PathVariable UUID playlistId,
                                                        @PathVariable UUID trackId) {

        PlaylistResponseDTO response = playlistService.addTrack(playlistId, trackId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping(value = "/playlists/{playlistId}/tracks/{trackId}")
    public ResponseEntity<PlaylistResponseDTO> removeTrack(@PathVariable UUID playlistId,
                                                           @PathVariable UUID trackId) {

        PlaylistResponseDTO response = playlistService.removeTrack(playlistId, trackId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping(value = "/playlists/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id){

        playlistService.delete(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
