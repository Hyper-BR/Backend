package br.com.hyper.services;

import br.com.hyper.dtos.requests.CartRequestDTO;
import br.com.hyper.dtos.responses.CartResponseDTO;
import br.com.hyper.entities.CustomerEntity;

import java.util.List;
import java.util.UUID;

public interface CartService {

    List<CartRequestDTO> getCartsByCustomerId(CustomerEntity customer);

    CartResponseDTO getCartTracks(UUID cartId, CustomerEntity customer);

    CartResponseDTO addTrackToCart(CustomerEntity customer, String trackId);

    CartResponseDTO removeTrackToCart(CustomerEntity customer, String trackId);


}
