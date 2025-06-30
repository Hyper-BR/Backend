package br.com.hyper.services;

import br.com.hyper.constants.ErrorCodes;
import br.com.hyper.dtos.requests.CartRequestDTO;
import br.com.hyper.dtos.requests.CustomerRequestDTO;
import br.com.hyper.dtos.responses.pages.ArtistPageResponseDTO;
import br.com.hyper.entities.CartEntity;
import br.com.hyper.enums.UserRole;
import br.com.hyper.exceptions.ArtistNotFoundException;
import br.com.hyper.exceptions.InvalidArtistDataException;
import br.com.hyper.dtos.requests.ArtistRequestDTO;
import br.com.hyper.entities.CustomerEntity;
import br.com.hyper.exceptions.CustomerException;
import br.com.hyper.dtos.responses.ArtistResponseDTO;
import br.com.hyper.repositories.ArtistRepository;
import br.com.hyper.repositories.CartRepository;
import br.com.hyper.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import br.com.hyper.entities.ArtistEntity;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArtistServiceImpl implements ArtistService {

    @Autowired
    private final ArtistRepository artistRepository;

    @Autowired
    private final ModelMapper modelMapper;

    @Autowired
    private final CustomerRepository customerRepository;

    @Autowired
    private final CartRepository cartRepository;

    @Override
    public ArtistResponseDTO save(ArtistRequestDTO artistDTO, CustomerEntity  customer) {
        try {

            ArtistEntity artist = modelMapper.map(artistDTO, ArtistEntity.class);
            artist.setCustomer(customer);
            artist.setCredits(0);
            artistRepository.save(artist);

            CartEntity cart = new CartEntity();
            cart.setName("Principal");
            cart.setTotalItems(0);
            cart.setTotalPrice(BigDecimal.ZERO);
            cart.setArtist(artist);

            artist.setCarts(List.of(cart));

            cartRepository.save(cart);

            List<ArtistEntity> artists = customer.getArtistProfiles();
            artists.add(artist);
            customer.setRole(UserRole.ARTIST);
            customer.setArtistProfiles(artists);
            customerRepository.save(customer);

            return modelMapper.map(artist, ArtistResponseDTO.class);
        } catch (DataIntegrityViolationException e) {
            throw new InvalidArtistDataException(ErrorCodes.DUPLICATED_DATA, ErrorCodes.DUPLICATED_DATA.getMessage());
        }
    }

    @Override
    public ArtistPageResponseDTO find(List<String> username, Pageable pageable) {

        Page<ArtistEntity> artistEntities;

        if(username != null){
            artistEntities = artistRepository.findByUsernamePage(username, pageable);
        } else {
            artistEntities = artistRepository.findAll(pageable);
        }

        return modelMapper.map(artistEntities, ArtistPageResponseDTO.class);
    }

    @Override
    public void delete(Long id) {
        ArtistEntity artistCurrent = findByIdOrThrowArtistDataNotFoundException(id);

        artistRepository.delete(artistCurrent);
    }

    @Override
    public ArtistResponseDTO addCart(Long id, CartRequestDTO cartRequestDTO) {
        ArtistEntity artist = findByIdOrThrowArtistDataNotFoundException(id);

        CartEntity cart = modelMapper.map(cartRequestDTO, CartEntity.class);
        cart.setTotalItems(0);
        cart.setTotalPrice(BigDecimal.ZERO);
        cart.setArtist(artist);

        artist.getCarts().add(cart);

        cartRepository.save(cart);

        return modelMapper.map(artist, ArtistResponseDTO.class);
    }

    private ArtistEntity findByIdOrThrowArtistDataNotFoundException(Long id) {
        return artistRepository.findById(id).orElseThrow(
                () -> new ArtistNotFoundException(ErrorCodes.DATA_NOT_FOUND, ErrorCodes.DATA_NOT_FOUND.getMessage()));
    }

    private CustomerEntity findByEmailOrThrowUserDataNotFoundException(String email) {
        return customerRepository.findByEmail(email).orElseThrow(
                () -> new CustomerException(ErrorCodes.DATA_NOT_FOUND, ErrorCodes.DATA_NOT_FOUND.getMessage()));
    }

}
