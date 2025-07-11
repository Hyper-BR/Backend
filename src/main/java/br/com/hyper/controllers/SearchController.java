package br.com.hyper.controllers;

import br.com.hyper.dtos.PageResponseDTO;
import br.com.hyper.dtos.responses.ArtistResponseDTO;
import br.com.hyper.dtos.responses.TrackResponseDTO;
import br.com.hyper.services.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @GetMapping(value = "/search/tracks")
    public ResponseEntity<PageResponseDTO<TrackResponseDTO>> searchTracks(@RequestParam(value = "page", defaultValue = "0", required = false) int page,
                                                                          @RequestParam("q") String query) {

        Pageable pageable = PageRequest.of(page, 10);

        PageResponseDTO<TrackResponseDTO> response = searchService.searchTracks(query, pageable);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(value = "/search/artists")
    public ResponseEntity<PageResponseDTO<ArtistResponseDTO>> searchArtists(@RequestParam(value = "page", defaultValue = "0", required = false) int page,
                                                                            @RequestParam("q") String query) {

        Pageable pageable = PageRequest.of(page, 10);

        PageResponseDTO<ArtistResponseDTO> response = searchService.searchArtists(query, pageable);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


}
