package br.com.hyper.services;

import br.com.hyper.constants.ErrorCodes;
import br.com.hyper.dtos.PageResponseDTO;
import br.com.hyper.dtos.requests.CustomerRequestDTO;
import br.com.hyper.dtos.responses.CustomerResponseDTO;
import br.com.hyper.exceptions.ArtistNotFoundException;
import br.com.hyper.exceptions.InvalidArtistDataException;
import br.com.hyper.dtos.requests.ArtistRequestDTO;
import br.com.hyper.entities.CustomerEntity;
import br.com.hyper.dtos.responses.ArtistResponseDTO;
import br.com.hyper.repositories.ArtistRepository;
import br.com.hyper.repositories.CustomerRepository;
import br.com.hyper.utils.PaginationMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import br.com.hyper.entities.ArtistEntity;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArtistServiceImpl implements ArtistService {

    private final ArtistRepository artistRepository;

    private final ModelMapper modelMapper;

    private final PaginationMapper paginationMapper;

    private final CustomerRepository customerRepository;

    @Override
    public ArtistResponseDTO becomeArtist(ArtistRequestDTO artistDTO, CustomerEntity customer) {
        try {
            if (artistRepository.existsByCustomer(customer)) {
                throw new InvalidArtistDataException(ErrorCodes.DUPLICATED_DATA, "Este usuário já possui um perfil de artista.");
            }

            ArtistEntity artist = modelMapper.map(artistDTO, ArtistEntity.class);
            artist.setUsername(artistDTO.getUsername().trim());
            artist.setIsVerified(false);
            artist.setCustomer(customer);

            artist = artistRepository.save(artist);

            return modelMapper.map(artist, ArtistResponseDTO.class);
        } catch (DataIntegrityViolationException e) {
            throw new InvalidArtistDataException(ErrorCodes.DUPLICATED_DATA, "Nome artístico já em uso.");
        }
    }

    @Override
    public PageResponseDTO<ArtistResponseDTO> find(Pageable pageable) {

        Page<ArtistEntity> entities = artistRepository.findAll(pageable);

        return paginationMapper.map(entities, ArtistResponseDTO.class);
    }

    @Override
    public ArtistResponseDTO findById(UUID id) {
        ArtistEntity artist = findByIdOrThrowArtistDataNotFoundException(id);

        return modelMapper.map(artist, ArtistResponseDTO.class);
    }

    @Override
    public ArtistResponseDTO update(UUID id, ArtistRequestDTO artistDTO) {
        ArtistEntity artistEntity = findByIdOrThrowArtistDataNotFoundException(id);

        if (artistDTO.getUsername() == null || artistDTO.getUsername().trim().isEmpty()) {
            throw new InvalidArtistDataException(ErrorCodes.INVALID_DATA, "Nome artístico não pode ser vazio.");
        }

        try {
            artistEntity.setUsername(artistDTO.getUsername().trim());

            artistEntity = artistRepository.save(artistEntity);

            return modelMapper.map(artistEntity, ArtistResponseDTO.class);
        } catch (DataIntegrityViolationException e) {
            throw new InvalidArtistDataException(ErrorCodes.DUPLICATED_DATA, "Nome artístico já em uso.");
        }
    }

    @Override
    public void delete(UUID id) {
        ArtistEntity artistCurrent = findByIdOrThrowArtistDataNotFoundException(id);

        artistRepository.delete(artistCurrent);
    }

    private ArtistEntity findByIdOrThrowArtistDataNotFoundException(UUID id) {
        return artistRepository.findById(id).orElseThrow(
                () -> new ArtistNotFoundException(ErrorCodes.DATA_NOT_FOUND, ErrorCodes.DATA_NOT_FOUND.getMessage()));
    }

}
