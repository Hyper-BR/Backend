package br.com.hyper.services;

import br.com.hyper.entities.CustomerEntity;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import java.util.UUID;

public interface StripeService {

    Session createSubscriptionCheckoutSession(CustomerEntity customer, Long planId) throws StripeException;

    void confirmSubscriptionPayment(String sessionId) throws StripeException;

    Session createCartCheckoutSession(CustomerEntity customer, UUID trackId) throws StripeException;

    void confirmCartPayment(String sessionId) throws StripeException;
}
