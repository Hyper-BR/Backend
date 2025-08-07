package br.com.hyper.services;

import br.com.hyper.dtos.requests.CartRequestDTO;
import br.com.hyper.dtos.responses.CartResponseDTO;
import br.com.hyper.entities.CartEntity;
import br.com.hyper.entities.CustomerEntity;
import br.com.hyper.enums.ErrorCodes;
import br.com.hyper.exceptions.GenericException;
import br.com.hyper.repositories.CartRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<CartRequestDTO> getCartsByCustomerId(CustomerEntity customer) {
        List<CartEntity> carts = cartRepository.findByCustomerId(customer.getId());
        return carts.stream()
                .map(cart -> modelMapper.map(cart, CartRequestDTO.class))
                .toList();
    }

    @Override
    public CartResponseDTO getCartTracks(UUID cartId, CustomerEntity customer) {
        Optional<CartEntity> cart = cartRepository.findById(cartId);

        if (cart.isPresent() && cart.get().getCustomer().getId().equals(customer.getId())) {
            return modelMapper.map(cart.get(), CartResponseDTO.class);
        } else {
            log.error("Cart with ID {} not found or does not belong to customer {}", cartId, customer.getId());
            throw new GenericException(ErrorCodes.UNAUTHORIZED, ErrorCodes.UNAUTHORIZED.getMessage());
        }
    }

    @Override
    public CartResponseDTO addTrackToCart(CustomerEntity customer, String trackId) {
        return null;
    }

    @Override
    public CartResponseDTO removeTrackToCart(CustomerEntity customer, String trackId) {
        return null;
    }


}
