package br.com.hyper.dtos;

import br.com.hyper.enums.SubscriptionType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class SubscriptionDTO {

    private Long id;
    private SubscriptionType type;
    private String name;
    private String description;
    private BigDecimal monthlyPrice;
}
