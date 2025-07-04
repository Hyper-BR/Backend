package br.com.hyper.controllers;

import br.com.hyper.dtos.AudioDTO;
import br.com.hyper.dtos.PageResponseDTO;
import br.com.hyper.dtos.requests.TrackRequestDTO;
import br.com.hyper.dtos.responses.TrackResponseDTO;
import br.com.hyper.services.AudioStreamingService;
import br.com.hyper.services.TrackService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.UUID;

@RestController
@Slf4j
@RequiredArgsConstructor
public class TrackController {

    private final TrackService trackService;

    private final AudioStreamingService audioStreamingService;

    @GetMapping(value = "/tracks")
    public ResponseEntity<PageResponseDTO<TrackResponseDTO>> find(
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "10", required = false) int size) {

        Pageable pageable = PageRequest.of(page, size);

        PageResponseDTO<TrackResponseDTO> response = trackService.find(pageable);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping(value = "/track/{id}")
    public ResponseEntity<TrackResponseDTO> update(@PathVariable UUID id, @RequestBody @Valid TrackRequestDTO track) {

        TrackResponseDTO response = trackService.update(id, track);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping(value = "/track/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id){

        trackService.delete(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/track/play/{id}")
    public ResponseEntity<InputStreamResource> streamAudio(@PathVariable UUID id, HttpServletRequest request) throws IOException {
        Resource resource = trackService.loadAudio(id);
        AudioDTO chunk = audioStreamingService.buildStreamResponse(resource, request);

        long contentLength = chunk.getEnd() - chunk.getStart() + 1;

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT_RANGES, "bytes");
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + chunk.getFilename() + "\"");
        headers.set(HttpHeaders.CONTENT_RANGE, "bytes " + chunk.getStart() + "-" + chunk.getEnd() + "/" + chunk.getTotal());

        return ResponseEntity.status(chunk.getStart() > 0 ? HttpStatus.PARTIAL_CONTENT : HttpStatus.OK)
                .headers(headers)
                .contentLength(contentLength)
                .contentType(chunk.getContentType())
                .body(new InputStreamResource(chunk.getStream()));
    }

}
