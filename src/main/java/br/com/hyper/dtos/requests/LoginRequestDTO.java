package br.com.hyper.dtos.requests;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class LoginRequestDTO {

    @NotEmpty(message = "Invalid email, can not be empty")
    String email;

    @NotEmpty(message = "Invalid password, can not be empty")
    String password;
}
