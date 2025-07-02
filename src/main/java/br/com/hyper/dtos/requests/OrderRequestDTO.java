package br.com.hyper.dtos.requests;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class OrderRequestDTO {

    private UUID id;

    private int totalItems;

    private BigDecimal totalPrice;
}
