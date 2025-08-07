package br.com.hyper.services;

import br.com.hyper.entities.CustomerEntity;
import br.com.hyper.entities.SubscriptionEntity;
import br.com.hyper.enums.ErrorCodes;
import br.com.hyper.enums.SubscriptionType;
import br.com.hyper.exceptions.GenericException;
import br.com.hyper.repositories.CustomerRepository;
import br.com.hyper.repositories.SubscriptionRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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
    private final CustomerRepository customerRepository;
    private final SubscriptionService subscriptionService;

    private void initStripe() {
        Stripe.apiKey = secretKey;
    }

    @Override
    public Session createSubscriptionCheckoutSession(CustomerEntity customer, Long planId) throws StripeException {
        initStripe();

        SubscriptionEntity subscription =findByIdOrThrowSubscriptionNotFoundException(planId);

        String priceId = getPriceIdForType(subscription.getType());

        Map<String, String> metadata = Map.of(
                "userId", customer.getId().toString(),
                "planId", subscription.getId().toString()
        );

        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
                .setSuccessUrl(successUrl)
                .setCancelUrl(cancelUrl)
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setPrice(priceId)
                                .setQuantity(1L)
                                .build()
                )
                .putAllMetadata(metadata)
                .build();

        try {
            return Session.create(params);
        } catch (StripeException e) {
            log.error("Error creating subscription checkout session for user: {} and plan: {}", customer.getId(), planId, e);
            throw new GenericException(ErrorCodes.STRIPE_EXCEPTION, e.getMessage());
        }
    }

    @Override
    public void confirmSubscriptionPayment(String sessionId) {
        initStripe();

        try {
            Session session = Session.retrieve(sessionId);

            if ("complete".equals(session.getStatus())) {
                UUID userId = UUID.fromString(session.getMetadata().get("userId"));
                Long planId = Long.parseLong(session.getMetadata().get("planId"));

                subscriptionService.activateSubscription(userId, planId);
            }
        } catch (StripeException e) {
            log.error("Error confirming payment for session: {}", sessionId, e);
            throw new RuntimeException("Failed to confirm payment", e);
        }
    }

    @Override
    public Session createCartCheckoutSession(CustomerEntity customer, UUID cartId) throws StripeException {
        initStripe();

        Map<String, String> metadata = Map.of(
                "customerId", customer.getId().toString(),
                "cartId", cartId.toString()
        );

        // Exemplo de preço fixo para o carrinho
        String cartPriceId = "price_1RtWT6Qs8Aq2jGSvCART123"; // substitua pelo ID real

        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(successUrl)
                .setCancelUrl(cancelUrl)
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setPrice(cartPriceId)
                                .setQuantity(1L)
                                .build()
                )
                .putAllMetadata(metadata)
                .build();

        return Session.create(params);
    }

    @Override
    public void confirmCartPayment(String sessionId) throws StripeException {
        initStripe();

        Session session = Session.retrieve(sessionId);

        if ("complete".equals(session.getStatus())) {
            UUID customerId = UUID.fromString(session.getMetadata().get("customerId"));
            UUID cartId = UUID.fromString(session.getMetadata().get("cartId"));

            // Aqui você pode chamar um método para finalizar o pedido
            log.info("Cart payment confirmed for customer: {} and cart: {}", customerId, cartId);
        }
    }

    private String getPriceIdForType(SubscriptionType type) {
        return switch (type) {
            case SOLO -> "price_1RtWT6Qs8Aq2jGSv7HSTKJoH";
            case ARTIST -> "price_1RtWT6Qs8Aq2jGSvJJxFj8W8";
            case LABEL -> "price_1RtWT6Qs8Aq2jGSv9aRJkhMr";
            default -> throw new GenericException(ErrorCodes.DATA_NOT_FOUND, "Invalid subscription type");
        };
    }

    private CustomerEntity findByIdOrThrowCustomerNotFoundException(UUID id) {
        return customerRepository.findById(id).orElseThrow(
                () -> new GenericException(ErrorCodes.DATA_NOT_FOUND, ErrorCodes.DATA_NOT_FOUND.getMessage()));
    }

    private SubscriptionEntity findByIdOrThrowSubscriptionNotFoundException(Long id) {
        return subscriptionRepository.findById(id).orElseThrow(
                () -> new GenericException(ErrorCodes.DATA_NOT_FOUND, ErrorCodes.DATA_NOT_FOUND.getMessage()));
    }
}
