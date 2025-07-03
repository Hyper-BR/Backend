package br.com.hyper.services;

import br.com.hyper.constants.ErrorCodes;
import br.com.hyper.dtos.PageResponseDTO;
import br.com.hyper.dtos.requests.PlaylistRequestDTO;
import br.com.hyper.dtos.responses.PlaylistResponseDTO;
import br.com.hyper.entities.CustomerEntity;
import br.com.hyper.entities.PlaylistEntity;
import br.com.hyper.entities.TrackEntity;
import br.com.hyper.exceptions.PlaylistNotFoundException;
import br.com.hyper.exceptions.TrackException;
import br.com.hyper.repositories.PlaylistRepository;
import br.com.hyper.repositories.TrackRepository;
import br.com.hyper.utils.PaginationMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlaylistServiceImpl implements PlaylistService {

    private final PlaylistRepository playlistRepository;

    private final TrackRepository trackRepository;

    private final ModelMapper modelMapper;

    private final PaginationMapper paginationMapper;

    @Override
    public PlaylistResponseDTO save(PlaylistRequestDTO playlist, CustomerEntity customer) {

        PlaylistEntity playlistEntity;
        try{
            if (playlist.getName() == null || playlist.getName().isEmpty()) {
                log.error("Playlist name cannot be null or empty");
                throw new PlaylistNotFoundException(ErrorCodes.DATA_NOT_FOUND, "Playlist name cannot be null or empty");
            }
            
            playlistEntity = modelMapper.map(playlist, PlaylistEntity.class);
            playlistEntity.setCustomer(customer);
            playlistEntity = playlistRepository.save(playlistEntity);

            return modelMapper.map(playlistEntity, PlaylistResponseDTO.class);

        }catch (DataIntegrityViolationException e){
            throw new DataIntegrityViolationException(e.getMessage());
        }
    }

    @Override
    public PageResponseDTO<PlaylistResponseDTO> find(Pageable pageable) {

        Page<PlaylistEntity> playlistEntities = playlistRepository.findAll(pageable);

        return paginationMapper.map(playlistEntities, PlaylistResponseDTO.class);
    }

    @Override
    public List<PlaylistResponseDTO> findByCustomer(UUID id) {
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
    public PlaylistResponseDTO update(UUID id, PlaylistRequestDTO playlist) {

        if (playlist == null || playlist.getName() == null || playlist.getName().isEmpty()) {
            log.error("Playlist data is invalid");
            throw new PlaylistNotFoundException(ErrorCodes.INVALID_DATA, "Playlist data cannot be null or empty");
        }

        if (playlist.getName().length() > 50) {
            log.error("Playlist name exceeds maximum length of 50 characters");
            throw new PlaylistNotFoundException(ErrorCodes.INVALID_DATA, "Playlist name exceeds maximum length of 50 characters");
        }

        PlaylistEntity playlistCurrent = findByIdOrThrowPlaylistDataNotFoundException(id);

        playlistCurrent.setName(playlist.getName());
        playlistCurrent.setDescription(playlist.getDescription());

        playlistRepository.save(playlistCurrent);

        return modelMapper.map(playlistCurrent, PlaylistResponseDTO.class);
    }

    @Override
    public void delete(UUID id) {

        PlaylistEntity playlistCurrent = findByIdOrThrowPlaylistDataNotFoundException(id);

        playlistRepository.delete(playlistCurrent);
    }

    @Override
    public PlaylistResponseDTO addTrack(UUID id, UUID trackId) {
        PlaylistEntity playlist = findByIdOrThrowPlaylistDataNotFoundException(id);
        TrackEntity track = findByIdOrThrowTrackDataNotFoundException(trackId);

        if (playlist.getTracks().contains(track)) {
            log.warn("Track with ID {} is already in the playlist with ID {}", trackId, id);
            return modelMapper.map(playlist, PlaylistResponseDTO.class);
        }

        playlist.getTracks().add(track);
        try {
            playlistRepository.save(playlist);
        } catch (DataIntegrityViolationException e) {
            log.error("Error adding track to playlist: {}", e.getMessage());
            throw new DataIntegrityViolationException("Error adding track to playlist: " + e.getMessage());
        }

        return modelMapper.map(playlist, PlaylistResponseDTO.class);
    }

    @Override
    public PlaylistResponseDTO removeTrack(UUID id, UUID trackId) {
        PlaylistEntity playlist = findByIdOrThrowPlaylistDataNotFoundException(id);
        TrackEntity track = findByIdOrThrowTrackDataNotFoundException(trackId);

        if (!playlist.getTracks().contains(track)) {
            log.warn("Track with ID {} is not in the playlist with ID {}", trackId, id);
            return modelMapper.map(playlist, PlaylistResponseDTO.class);
        }

        playlist.getTracks().remove(track);
        try {
            playlistRepository.save(playlist);
        } catch (DataIntegrityViolationException e) {
            log.error("Error removing track from playlist: {}", e.getMessage());
            throw new DataIntegrityViolationException("Error removing track from playlist: " + e.getMessage());
        }

        return modelMapper.map(playlist, PlaylistResponseDTO.class);
    }

    private PlaylistEntity findByIdOrThrowPlaylistDataNotFoundException(UUID id) {
        return playlistRepository.findById(id).orElseThrow(
                () -> new PlaylistNotFoundException(ErrorCodes.DATA_NOT_FOUND, "Playlist not found with ID: " + id));
    }
    private TrackEntity findByIdOrThrowTrackDataNotFoundException(UUID id) {
        return trackRepository.findById(id).orElseThrow(
                () -> new TrackException(ErrorCodes.DATA_NOT_FOUND, "Track not found with ID: " + id));
    }
}
