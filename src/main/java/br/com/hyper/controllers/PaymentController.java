package br.com.hyper.controllers;

import br.com.hyper.dtos.SubscriptionDTO;
import br.com.hyper.dtos.requests.TrackRequestDTO;
import br.com.hyper.entities.CustomerEntity;
import br.com.hyper.services.StripeService;
import com.stripe.exception.StripeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.stripe.model.checkout.Session;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
public class PaymentController {

    private final StripeService stripeService;

    @PostMapping(value = "/payment/subscription/checkout")
    public ResponseEntity<Map<String, String>> subscriptionCheckout(@RequestBody SubscriptionDTO payload,
                                                                    @AuthenticationPrincipal CustomerEntity customer){

        Session session = stripeService.createSubscriptionCheckoutSession(customer, payload.getId());

        return ResponseEntity.ok(Map.of("redirectUrl", session.getUrl()));
    }

    @PostMapping(value = "/payment/subscription/confirm")
    public ResponseEntity<Void> subscriptionConfirm(@RequestParam String sessionId){

        stripeService.confirmSubscriptionPayment(sessionId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping(value = "/payment/cart/checkout")
    public ResponseEntity<Map<String, String>> cartCheckout(@RequestBody TrackRequestDTO payload,
                                                            @AuthenticationPrincipal CustomerEntity customer){

        Session session = stripeService.createCartCheckoutSession(customer, payload.getId());

        return ResponseEntity.ok(Map.of("redirectUrl", session.getUrl()));
    }

    @PostMapping(value = "/payment/cart/confirm")
    public ResponseEntity<Void> cartConfirm(@RequestParam String sessionId){

        stripeService.confirmCartPayment(sessionId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}