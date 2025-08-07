package br.com.hyper.dtos.requests;

import lombok.Data;

import java.util.UUID;

@Data
public class CartRequestDTO {

    private UUID id;
    private String name;
}
