package br.com.hyper.services;

import br.com.hyper.clients.PaymentGatewayClient;
import br.com.hyper.dtos.SubscriptionDTO;
import br.com.hyper.entities.CustomerEntity;
import br.com.hyper.entities.SubscriptionEntity;
import br.com.hyper.enums.ErrorCodes;
import br.com.hyper.exceptions.GenericException;
import br.com.hyper.repositories.CustomerRepository;
import br.com.hyper.repositories.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    private final CustomerRepository customerRepository;

    private final PaymentGatewayClient paymentGatewayClient;

    private final ModelMapper modelMapper;

    @Override
    public List<SubscriptionDTO> findAll() {
        List<SubscriptionEntity> response = subscriptionRepository.findAll();

        return response.stream()
                .map(subscription -> modelMapper.map(subscription, SubscriptionDTO.class))
                .toList();
    }

    @Override
    public void activateSubscription(UUID customerId, Long planId) {
        CustomerEntity customer = findByIdOrThrowCustomerNotFoundException(customerId);

        SubscriptionEntity subscription = findByIdOrThrowSubscriptionNotFoundException(planId);

        log.info("Activating subscription for customer: {}, plan: {}", customerId, planId);

        customer.setSubscription(subscription);

        customerRepository.save(customer);

        log.info("Subscription activated for customer: {}, plan: {}", customerId, planId);
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
