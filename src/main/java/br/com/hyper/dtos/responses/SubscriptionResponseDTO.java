package br.com.hyper.dtos.responses;

import br.com.hyper.entities.SubscriptionEntity;
import br.com.hyper.enums.SubscriptionOption;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.List;

@Data
public class SubscriptionResponseDTO {

    private SubscriptionOption option;
}
