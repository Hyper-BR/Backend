package br.com.hyper.services;

import br.com.hyper.constants.ErrorCodes;
import br.com.hyper.dtos.PageResponseDTO;
import br.com.hyper.dtos.requests.ArtistRequestDTO;
import br.com.hyper.entities.CustomerEntity;
import br.com.hyper.dtos.responses.ArtistResponseDTO;
import br.com.hyper.exceptions.GenericException;
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
    private final CustomerRepository customerRepository;

    private final ArtistRepository artistRepository;

    private final ModelMapper modelMapper;

    private final PaginationMapper paginationMapper;

    @Override
    public ArtistResponseDTO becomeArtist(ArtistRequestDTO artistDTO, CustomerEntity customer) {
        try {
            if (artistRepository.existsByCustomer(customer)) {
                throw new GenericException(ErrorCodes.DUPLICATED_DATA, ErrorCodes.DUPLICATED_DATA.getMessage());
            }

            if (Boolean.TRUE.equals(customer.getIsLabel()))  {
                throw new GenericException(ErrorCodes.INVALID_DATA, ErrorCodes.INVALID_DATA.getMessage());
            }

            ArtistEntity artist = modelMapper.map(artistDTO, ArtistEntity.class);
            artist.setUsername(artistDTO.getUsername().trim());
            artist.setIsVerified(false);
            artist.setFreeTrackLimit(5);

            customer.setIsArtist(true);
            customer = customerRepository.save(customer);

            artist.setCustomer(customer);
            artist = artistRepository.save(artist);

            return modelMapper.map(artist, ArtistResponseDTO.class);
        } catch (Exception e) {
            throw new GenericException(ErrorCodes.INVALID_DATA, e.getMessage());
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
            throw new GenericException(ErrorCodes.INVALID_DATA, ErrorCodes.INVALID_DATA.getMessage());
        }

        try {
            artistEntity.setUsername(artistDTO.getUsername().trim());

            artistEntity = artistRepository.save(artistEntity);

            return modelMapper.map(artistEntity, ArtistResponseDTO.class);
        } catch (DataIntegrityViolationException e) {
            throw new GenericException(ErrorCodes.DUPLICATED_DATA, ErrorCodes.DUPLICATED_DATA.getMessage());
        }
    }

    @Override
    public void delete(UUID id) {
        ArtistEntity artistCurrent = findByIdOrThrowArtistDataNotFoundException(id);

        artistRepository.delete(artistCurrent);
    }

    private ArtistEntity findByIdOrThrowArtistDataNotFoundException(UUID id) {
        return artistRepository.findById(id).orElseThrow(
                () -> new GenericException(ErrorCodes.DATA_NOT_FOUND, ErrorCodes.DATA_NOT_FOUND.getMessage()));
    }
}
