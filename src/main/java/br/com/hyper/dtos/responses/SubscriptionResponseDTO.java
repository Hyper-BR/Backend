package br.com.hyper.dtos.responses;

import br.com.hyper.enums.SubscriptionOption;
import lombok.Data;

@Data
public class SubscriptionResponseDTO {

    private SubscriptionOption option;
}
