package br.com.hyper.controllers;

import br.com.hyper.dtos.responses.pages.TrackPageResponseDTO;
import br.com.hyper.services.TrackService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
public class TrackController {

    @Autowired
    private TrackService trackService;

    @GetMapping(value = "/track")
    public ResponseEntity<TrackPageResponseDTO> find(
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "sort", defaultValue = "UNSORT", required = false) String sort,
            @RequestParam(value = "size", defaultValue = "10", required = false) int size) {

        Pageable pageable = PageRequest.of(page, size);

        TrackPageResponseDTO response = trackService.find(pageable);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
