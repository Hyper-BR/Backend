package br.com.hyper.services;

import br.com.hyper.dtos.responses.pages.PlaylistPageReponseDTO;
import br.com.hyper.dtos.responses.pages.TrackPageResponseDTO;
import br.com.hyper.entities.PlaylistEntity;
import br.com.hyper.entities.TrackEntity;
import br.com.hyper.repositories.ReleaseRepository;
import br.com.hyper.repositories.TrackRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrackServiceImpl implements TrackService {

    @Autowired
    private final TrackRepository trackRepository;

    @Autowired
    private final ModelMapper modelMapper;

    @Override
    public TrackPageResponseDTO find(Pageable pageable) {

        Page<TrackEntity> trackEntities;

        trackEntities = trackRepository.findAll(pageable);

        return modelMapper.map(trackEntities, TrackPageResponseDTO.class);
    }
}
