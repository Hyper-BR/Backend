package br.com.hyper.dtos.responses;

import lombok.Data;

@Data
public class LoginResponseDTO {

    private CustomerResponseDTO customer;
    private TokenResponseDTO token;
}
