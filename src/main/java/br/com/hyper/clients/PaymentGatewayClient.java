package br.com.hyper.clients;

import br.com.hyper.entities.CustomerEntity;
import org.springframework.stereotype.Component;

@Component
public class PaymentGatewayClient {

    public String createCheckoutSession(CustomerEntity customer, Long planId) {
        return "https://pagamento.fake/checkout?user=" + customer.getId() + "&plan=" + planId;
    }
}
