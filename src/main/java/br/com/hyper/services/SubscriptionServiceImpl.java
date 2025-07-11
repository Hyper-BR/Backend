package br.com.hyper.services;

import br.com.hyper.dtos.responses.SubscriptionResponseDTO;
import br.com.hyper.entities.SubscriptionEntity;
import br.com.hyper.repositories.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    private final ModelMapper modelMapper;

    @Override
    public List<SubscriptionResponseDTO> findAll() {
        List<SubscriptionEntity> response = subscriptionRepository.findAll();

        return response.stream()
                .map(subscription -> modelMapper.map(subscription, SubscriptionResponseDTO.class))
                .toList();
    }

}
