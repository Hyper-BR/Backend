package br.com.hyper.dtos.requests;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class AuthRequestDTO {

    @NotEmpty(message = "Invalid email, can not be empty")
    private String email;

    @NotEmpty(message = "Invalid password, can not be empty")
    private String password;
}
