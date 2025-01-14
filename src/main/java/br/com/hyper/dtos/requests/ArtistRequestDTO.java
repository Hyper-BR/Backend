package br.com.hyper.dtos.requests;

import lombok.Data;
import javax.validation.constraints.NotEmpty;

@Data
public class ArtistRequestDTO {

    private Long id;

    @NotEmpty(message = "Invalid username, can not be empty")
    private String username;

    @NotEmpty(message = "Invalid email, can not be empty")
    private String email;

}
