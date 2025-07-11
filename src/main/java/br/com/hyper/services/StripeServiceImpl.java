package br.com.hyper.services;

import br.com.hyper.entities.SubscriptionEntity;
import br.com.hyper.repositories.SubscriptionRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class StripeServiceImpl implements StripeService {

    @Value("${stripe.secret.key}")
    private String secretKey;

    @Value("${stripe.checkout.success-url}")
    private String successUrl;

    @Value("${stripe.checkout.cancel-url}")
    private String cancelUrl;

    private final SubscriptionRepository subscriptionRepository;

    private final SubscriptionService subscriptionService;

    public Session createCheckoutSession(UUID userId, Long planId) throws StripeException {
        Stripe.apiKey = secretKey;

        SubscriptionEntity subscription = subscriptionRepository.findById(planId)
                .orElseThrow(() -> new IllegalArgumentException("Subscription not found with id: " + planId));

        BigDecimal price = subscription.getMonthlyPrice();
        Long priceInCents = price.multiply(BigDecimal.valueOf(100)).longValue();

        Map<String, String> metadata = Map.of(
                "userId", userId.toString(),
                "planId", planId.toString()
        );

        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(successUrl)
                .setCancelUrl(cancelUrl)
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .addLineItem(SessionCreateParams.LineItem.builder()
                        .setQuantity(1L)
                        .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency("brl")
                                .setUnitAmount(priceInCents)
                                .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                        .setName(subscription.getName())
                                        .build())
                                .build())
                        .build())
                .putAllMetadata(metadata)
                .build();

        return Session.create(params);
    }

    @Override
    public void confirmPayment(String sessionId) {

        try {
            Stripe.apiKey = secretKey;
            Session session = Session.retrieve(sessionId);

            if ("complete".equals(session.getStatus())) {
                UUID userId = UUID.fromString(session.getMetadata().get("userId"));
                Long planId = Long.parseLong(session.getMetadata().get("planId"));

                subscriptionService.activateSubscription(userId, planId);
            }
        }
        catch (StripeException e) {
            log.error("Error confirming payment for session: {}", sessionId, e);
            throw new RuntimeException("Failed to confirm payment", e);
        }
    }
}
