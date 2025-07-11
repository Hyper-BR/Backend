package br.com.hyper.controllers;

import br.com.hyper.services.SubscriptionService;
import com.stripe.Stripe;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@Slf4j
@RequiredArgsConstructor
public class StripeWebhookController {

    @Value("${stripe.secret.key}")
    private String secretKey;

    @Value("${stripe.webhook.secret}")
    private String webhookSecret;

    private final SubscriptionService subscriptionService;

    @PostMapping("/stripe/webhook")
    public ResponseEntity<Void> handle(@RequestBody String payload,
                                       @RequestHeader("Stripe-Signature") String sigHeader) throws Exception {
        Stripe.apiKey = secretKey;

        Event event = Webhook.constructEvent(payload, sigHeader, webhookSecret);

        if ("checkout.session.completed".equals(event.getType())) {
            Session session = (Session) event.getDataObjectDeserializer().getObject().orElseThrow();

            String userId = session.getMetadata().get("userId");
            String planId = session.getMetadata().get("planId");

            // ✅ Aqui você ativa o plano no banco
            subscriptionService.activateSubscription(UUID.fromString(userId), Long.parseLong(planId));
        }

        return ResponseEntity.ok().build();
    }
}

