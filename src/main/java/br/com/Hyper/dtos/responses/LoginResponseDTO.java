package br.com.hyper.dtos.responses;

import lombok.Data;

@Data
public class LoginResponseDTO {

    private String id;
    private String email;
    private TokenResponseDTO token;
}
