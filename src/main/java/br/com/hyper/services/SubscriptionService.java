package br.com.hyper.services;

import br.com.hyper.dtos.responses.SubscriptionResponseDTO;

import java.util.List;

public interface SubscriptionService {

    List<SubscriptionResponseDTO> findAll();
}
