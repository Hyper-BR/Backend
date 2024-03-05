package br.com.hyper.controllers;

import br.com.hyper.dtos.requests.PlaylistRequestDTO;
import br.com.hyper.dtos.responses.PlaylistResponseDTO;
import br.com.hyper.dtos.responses.pages.PlaylistPageReponseDTO;
import br.com.hyper.services.PlaylistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Slf4j
@RequiredArgsConstructor
public class PlaylistController {

    @Autowired
    private final PlaylistService playlistService;

    @PostMapping(value = "/playlist")
    public ResponseEntity<PlaylistResponseDTO> save(
            @RequestBody @Valid PlaylistRequestDTO playlist) {

        PlaylistResponseDTO response = playlistService.save(playlist);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping(value = "/playlist")
    public ResponseEntity<PlaylistPageReponseDTO> find(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "sort", defaultValue = "UNSORT", required = false) String sort,
            @RequestParam(value = "size", defaultValue = "10", required = false) int size) {

        Pageable pageable = PageRequest.of(page, size);

        PlaylistPageReponseDTO response = playlistService.find(name, pageable);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping(value = "/playlist/{id}")
    public ResponseEntity<PlaylistResponseDTO> updateName(
                                                            @PathVariable Long id,
                                                            @RequestBody @Valid PlaylistRequestDTO playlist) {

        PlaylistResponseDTO response = playlistService.updateName(id, playlist.getName());

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping(value = "/playlist/{id}/add-track")
    public ResponseEntity<PlaylistResponseDTO> addTrack(
                                                        @PathVariable Long id,
                                                        @RequestBody Long trackId) {

        PlaylistResponseDTO response = playlistService.addTrack(id, trackId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping(value = "/playlist/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){

        playlistService.delete(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
