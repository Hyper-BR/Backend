package br.com.hyper.controllers;

import br.com.hyper.dtos.requests.TrackRequestDTO;
import br.com.hyper.dtos.responses.pages.TrackPageResponseDTO;
import br.com.hyper.entities.CustomerEntity;
import br.com.hyper.services.TrackService;
import br.com.hyper.dtos.responses.TrackResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class TrackController {

    @Autowired
    private final TrackService trackService;

    @PostMapping(value = "/track", consumes = { "multipart/form-data" })
    public ResponseEntity<TrackResponseDTO> create(
            @RequestParam(value = "artistId") Long artistId,
            @ModelAttribute(value = "track") TrackRequestDTO track,
            @AuthenticationPrincipal CustomerEntity customer) throws IOException {

        TrackResponseDTO response = trackService.save(track, artistId);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping(value = "/track")
    public ResponseEntity<TrackPageResponseDTO> find(
            @RequestParam(value = "genres", required = false) List<String> genres,
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "sort", defaultValue = "UNSORT", required = false) String sort,
            @RequestParam(value = "size", defaultValue = "10", required = false) int size) {

        Pageable pageable = PageRequest.of(page, size);

        TrackPageResponseDTO response = trackService.find(genres, pageable);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(value = "/track/{id}")
    public ResponseEntity<TrackResponseDTO> findById(@PathVariable Long id) {

        TrackResponseDTO response = trackService.findById(id);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(value = "/track/{id}/url")
    public String getTrackUrl(@PathVariable Long id) {

        return trackService.getTrackUrl(id);
    }

    @PutMapping(value = "/track/{id}")
    public ResponseEntity<TrackResponseDTO> update(@PathVariable Long id, @RequestBody TrackRequestDTO music) {

        TrackResponseDTO response = trackService.update(id, music);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping(value = "/track/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){

        trackService.delete(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping(value = "/track/download/{id}")
    public byte[] downloadTrack(@PathVariable Long id) {

        return trackService.downloadTrack(id);
    }
}
