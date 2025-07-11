package br.com.hyper.controllers;

import br.com.hyper.dtos.SubscriptionDTO;
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

    @PostMapping(value = "/payment/checkout")
    public ResponseEntity<Map<String, String>> checkout(@RequestBody SubscriptionDTO payload,
                                                        @AuthenticationPrincipal CustomerEntity customer) throws StripeException {

        Session session = stripeService.createCheckoutSession(customer.getId(), payload.getId());

        return ResponseEntity.ok(Map.of("redirectUrl", session.getUrl()));
    }

    @PostMapping(value = "/payment/confirm")
    public ResponseEntity<Void> confirm(@RequestParam String sessionId) throws StripeException {

        stripeService.confirmPayment(sessionId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
