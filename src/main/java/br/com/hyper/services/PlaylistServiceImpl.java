package br.com.hyper.services;

import br.com.hyper.constants.ErrorCodes;
import br.com.hyper.dtos.requests.PlaylistRequestDTO;
import br.com.hyper.dtos.responses.PlaylistResponseDTO;
import br.com.hyper.dtos.responses.pages.PlaylistPageReponseDTO;
import br.com.hyper.entities.CustomerEntity;
import br.com.hyper.entities.PlaylistEntity;
import br.com.hyper.entities.TrackEntity;
import br.com.hyper.exceptions.PlaylistNotFoundException;
import br.com.hyper.exceptions.TrackException;
import br.com.hyper.repositories.TrackRepository;
import br.com.hyper.repositories.PlaylistRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlaylistServiceImpl implements PlaylistService {

    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private TrackRepository trackRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public PlaylistResponseDTO save(PlaylistRequestDTO playlist, CustomerEntity customer) {

        PlaylistEntity playlistEntity = new PlaylistEntity();
        try{
            if (playlist.getName() == null || playlist.getName().isEmpty()) {
                log.error("Playlist name cannot be null or empty");
                throw new PlaylistNotFoundException(ErrorCodes.DATA_NOT_FOUND, "Playlist name cannot be null or empty");
            }
            
            playlistEntity.setName(playlist.getName());

//            playlistEntity = modelMapper.map(playlist, PlaylistEntity.class);
            playlistEntity.setCustomer(customer);
            playlistEntity = playlistRepository.save(playlistEntity);

            return modelMapper.map(playlistEntity, PlaylistResponseDTO.class);

        }catch (DataIntegrityViolationException e){
            throw new DataIntegrityViolationException(e.getMessage());
        }
    }

    @Override
    public PlaylistPageReponseDTO find(String name, Pageable pageable) {

        Page<PlaylistEntity> playlistEntities;

        if(name != null){
            playlistEntities = playlistRepository.findByName(name, pageable);
        } else {
            playlistEntities = playlistRepository.findAll(pageable);
        }

        return modelMapper.map(playlistEntities, PlaylistPageReponseDTO.class);
    }

    @Override
    public List<PlaylistResponseDTO> findByCustomer(Long id) {
        List<PlaylistEntity> playlists;

        if (id == null) {
            log.error("Customer ID is null");
            throw new PlaylistNotFoundException(ErrorCodes.DATA_NOT_FOUND, "Customer ID cannot be null");
        }

        try {
            playlists = playlistRepository.findByCustomerId(id);

        } catch (Exception e) {
            log.error("Error finding playlists for customer with ID: {}", id, e);
            throw new PlaylistNotFoundException(ErrorCodes.DATA_NOT_FOUND, "Playlists not found for customer ID: " + id);
        }

        return playlists.stream()
                .map(playlist -> modelMapper.map(playlist, PlaylistResponseDTO.class))
                .toList();
    }

    @Override
    public PlaylistResponseDTO update(Long id, PlaylistRequestDTO playlist) {
        PlaylistEntity playlistCurrent = findByIdOrThrowPlaylistDataNotFoundException(id);

        playlistCurrent.setName(playlist.getName());

        playlistRepository.save(playlistCurrent);

        return modelMapper.map(playlistCurrent, PlaylistResponseDTO.class);
    }

    @Override
    public void delete(Long id) {
        PlaylistEntity playlistCurrent = findByIdOrThrowPlaylistDataNotFoundException(id);

        playlistRepository.delete(playlistCurrent);
    }

    @Override
    public PlaylistResponseDTO addTrack(Long id, Long trackId) {
        PlaylistEntity playlist = findByIdOrThrowPlaylistDataNotFoundException(id);
        TrackEntity track = findByIdOrThrowTrackDataNotFoundException(id);
        playlist.getTracks().add(track);
        return modelMapper.map(playlist, PlaylistResponseDTO.class);
    }

    @Override
    public PlaylistResponseDTO updateName(Long id, String name) {
        PlaylistEntity playlist = findByIdOrThrowPlaylistDataNotFoundException(id);

        playlist.setName(name);

        playlistRepository.save(playlist);

        return modelMapper.map(playlist, PlaylistResponseDTO.class);
    }

    private PlaylistEntity findByIdOrThrowPlaylistDataNotFoundException(Long id) {
        return playlistRepository.findById(id).orElseThrow(
                () -> new PlaylistNotFoundException(ErrorCodes.DATA_NOT_FOUND, ErrorCodes.DATA_NOT_FOUND.getMessage()));
    }
    private TrackEntity findByIdOrThrowTrackDataNotFoundException(Long id) {
        return trackRepository.findById(id).orElseThrow(
                () -> new TrackException(ErrorCodes.DATA_NOT_FOUND, ErrorCodes.DATA_NOT_FOUND.getMessage()));
    }
}
