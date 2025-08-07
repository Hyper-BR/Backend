package br.com.hyper.services;

import br.com.hyper.entities.CustomerEntity;
import com.stripe.model.checkout.Session;
import java.util.UUID;

public interface StripeService {

    Session createSubscriptionCheckoutSession(CustomerEntity customer, Long planId);

    void confirmSubscriptionPayment(String sessionId);

    Session createCartCheckoutSession(CustomerEntity customer, UUID trackId);

    void confirmCartPayment(String sessionId);
}
