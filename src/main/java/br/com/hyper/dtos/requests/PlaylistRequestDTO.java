package br.com.hyper.dtos.requests;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.UUID;

@Data
public class PlaylistRequestDTO {

    private UUID id;

    @NotEmpty(message = "Invalid name, can not be empty")
    private String name;
}
