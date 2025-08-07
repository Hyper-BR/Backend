package br.com.hyper.controllers;

import br.com.hyper.dtos.requests.CartRequestDTO;
import br.com.hyper.dtos.responses.CartResponseDTO;
import br.com.hyper.entities.CustomerEntity;
import br.com.hyper.services.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@Slf4j
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping(value = "/carts/customer")
    public ResponseEntity<List<CartRequestDTO>> getCustomerCarts(@AuthenticationPrincipal CustomerEntity customer) {

        List<CartRequestDTO> response = cartService.getCartsByCustomerId(customer);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(value = "/cart/{cartId}/tracks")
    public ResponseEntity<CartResponseDTO> getCartTracks(@AuthenticationPrincipal CustomerEntity customer,
                                                         @PathVariable("cartId") UUID cartId) {

        CartResponseDTO response = cartService.getCartTracks(cartId, customer);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
