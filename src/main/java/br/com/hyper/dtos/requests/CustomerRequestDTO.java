package br.com.hyper.dtos.requests;

import lombok.Data;

@Data
public class CustomerRequestDTO {

    private String name;

    private String password;

    private String email;

    private String birthDate;

    private Long subscription;

    private String country;

    private String avatarUrl;
}
