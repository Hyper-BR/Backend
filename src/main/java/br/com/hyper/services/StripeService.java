package br.com.hyper.services;

import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import java.util.UUID;

public interface StripeService {

    Session createCheckoutSession(UUID userId, Long planId) throws StripeException;

    void confirmPayment(String sessionId) throws StripeException;
}
