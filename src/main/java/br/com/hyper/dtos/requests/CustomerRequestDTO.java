package br.com.hyper.dtos.requests;

import br.com.hyper.enums.UserRole;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class CustomerRequestDTO {

    private Long id;

    @NotEmpty(message = "Invalid name, can not be empty")
    private String name;

    @NotEmpty(message = "Invalid password, can not be empty")
    private String password;

    @NotEmpty(message = "Invalid email, can not be empty")
    private String email;

    @NotEmpty(message = "Invalid birthDate, can not be empty")
    private String birthDate;

    @NotEmpty(message = "Invalid subscription, can not be empty")
    private Long subscription;

    @NotEmpty(message = "Invalid country, can not be empty")
    private String country;

    private String avatar;

    private UserRole role;
}
