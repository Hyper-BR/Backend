package br.com.hyper.services;

import br.com.hyper.dtos.SubscriptionDTO;

import java.util.List;
import java.util.UUID;

public interface SubscriptionService {

    List<SubscriptionDTO> findAll();

    void activateSubscription(UUID customerId, Long planId);
}
