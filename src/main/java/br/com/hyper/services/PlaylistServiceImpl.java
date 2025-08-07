package br.com.hyper.services;

import br.com.hyper.constants.BaseUrls;
import br.com.hyper.enums.ErrorCodes;
import br.com.hyper.dtos.PageResponseDTO;
import br.com.hyper.dtos.requests.PlaylistRequestDTO;
import br.com.hyper.dtos.responses.PlaylistResponseDTO;
import br.com.hyper.entities.CustomerEntity;
import br.com.hyper.entities.PlaylistEntity;
import br.com.hyper.entities.TrackEntity;
import br.com.hyper.enums.Privacy;
import br.com.hyper.exceptions.GenericException;
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
                throw new GenericException(ErrorCodes.INVALID_DATA, "Playlist name cannot be null or empty.");
            }

            if (playlist.getName().length() > 30) {
                throw new GenericException(ErrorCodes.INVALID_DATA, "Playlist name is too long. Maximum length is 30 characters.");
            }
            
            playlistEntity = modelMapper.map(playlist, PlaylistEntity.class);
            playlistEntity.setCustomer(customer);
            playlistEntity.setCoverUrl(BaseUrls.PLAYLIST_URL);
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
    public PlaylistResponseDTO findById(UUID id) {
        PlaylistEntity playlist = findByIdOrThrowPlaylistDataNotFoundException(id);

        return modelMapper.map(playlist, PlaylistResponseDTO.class);
    }

    @Override
    public List<PlaylistResponseDTO> findByCustomer(UUID id) {
        List<PlaylistEntity> playlists;

        if (id == null) {
            throw new GenericException(ErrorCodes.DATA_NOT_FOUND, ErrorCodes.DATA_NOT_FOUND.getMessage());
        }

        try {
            playlists = playlistRepository.findByCustomerId(id);

        } catch (java.lang.Exception e) {
            throw new GenericException(ErrorCodes.DATA_NOT_FOUND, ErrorCodes.DATA_NOT_FOUND.getMessage());
        }

        return playlists.stream()
                .map(playlist -> modelMapper.map(playlist, PlaylistResponseDTO.class))
                .toList();
    }

    @Override
    public List<PlaylistResponseDTO> findByArtist(UUID id) {
        List<PlaylistEntity> playlists;

        if (id == null) {
            throw new GenericException(ErrorCodes.DATA_NOT_FOUND, ErrorCodes.DATA_NOT_FOUND.getMessage());
        }

        try {
            playlists = playlistRepository.findByArtistId(id, Privacy.PUBLIC);

        } catch (java.lang.Exception e) {
            throw new GenericException(ErrorCodes.DATA_NOT_FOUND, ErrorCodes.DATA_NOT_FOUND.getMessage());
        }

        return playlists.stream()
                .map(playlist -> modelMapper.map(playlist, PlaylistResponseDTO.class))
                .toList();
    }

    @Override
    public PlaylistResponseDTO update(UUID id, PlaylistRequestDTO playlist) {

        if (playlist == null || playlist.getName() == null || playlist.getName().isEmpty()) {
            throw new GenericException(ErrorCodes.INVALID_DATA, ErrorCodes.INVALID_DATA.getMessage());
        }

        if (playlist.getName().length() > 50) {
            throw new GenericException(ErrorCodes.INVALID_DATA, ErrorCodes.INVALID_DATA.getMessage());
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
            return modelMapper.map(playlist, PlaylistResponseDTO.class);
        }

        playlist.getTracks().add(track);
        playlistRepository.save(playlist);

        return modelMapper.map(playlist, PlaylistResponseDTO.class);
    }

    @Override
    public PlaylistResponseDTO removeTrack(UUID id, UUID trackId) {
        PlaylistEntity playlist = findByIdOrThrowPlaylistDataNotFoundException(id);
        TrackEntity track = findByIdOrThrowTrackDataNotFoundException(trackId);

        if (!playlist.getTracks().contains(track)) {
            return modelMapper.map(playlist, PlaylistResponseDTO.class);
        }

        playlist.getTracks().remove(track);
        playlistRepository.save(playlist);

        return modelMapper.map(playlist, PlaylistResponseDTO.class);
    }

    private PlaylistEntity findByIdOrThrowPlaylistDataNotFoundException(UUID id) {
        return playlistRepository.findById(id).orElseThrow(
                () -> new GenericException(ErrorCodes.DATA_NOT_FOUND, ErrorCodes.DATA_NOT_FOUND.getMessage()));
    }

    private TrackEntity findByIdOrThrowTrackDataNotFoundException(UUID id) {
        return trackRepository.findById(id).orElseThrow(
                () -> new GenericException(ErrorCodes.DATA_NOT_FOUND, ErrorCodes.DATA_NOT_FOUND.getMessage()));
    }
}
