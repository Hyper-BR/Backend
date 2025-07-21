package br.com.hyper.dtos.requests;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CustomerRequestDTO {

    private String name;

    private String password;

    private String biography;

    private String email;

    private String birthDate;

    private String country;

    private MultipartFile avatar;

    private MultipartFile cover;
}
