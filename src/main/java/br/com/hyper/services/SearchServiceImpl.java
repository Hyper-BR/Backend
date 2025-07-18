package br.com.hyper.services;

import br.com.hyper.dtos.PageResponseDTO;
import br.com.hyper.dtos.responses.ArtistResponseDTO;
import br.com.hyper.dtos.responses.CustomerResponseDTO;
import br.com.hyper.dtos.responses.TrackResponseDTO;
import br.com.hyper.entities.ArtistEntity;
import br.com.hyper.entities.TrackEntity;
import br.com.hyper.enums.Privacy;
import br.com.hyper.repositories.ArtistRepository;
import br.com.hyper.repositories.TrackRepository;
import br.com.hyper.utils.PaginationMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {

    private final TrackRepository trackRepository;

    private final ArtistRepository artistRepository;

    private final PaginationMapper paginationMapper;

    public PageResponseDTO<ArtistResponseDTO> searchArtists(String q, Pageable pageable) {
        Page<ArtistEntity> artists = artistRepository.searchByName(q, pageable);

        return paginationMapper.map(artists, ArtistResponseDTO.class);
    }

    public PageResponseDTO<TrackResponseDTO> searchTracks(String q, Pageable pageable) {

        Page<TrackEntity> tracks = trackRepository.searchByTitleOrArtist(q, Privacy.PUBLIC, pageable);

        return paginationMapper.map(tracks, TrackResponseDTO.class);

    }
}
