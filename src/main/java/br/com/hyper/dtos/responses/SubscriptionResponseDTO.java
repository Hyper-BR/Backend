package br.com.hyper.dtos.responses;

import br.com.hyper.enums.SubscriptionType;
import lombok.Data;

@Data
public class SubscriptionResponseDTO {

    private SubscriptionType type;
}
