package br.com.hyper.dtos.requests;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class PlaylistRequestDTO {

    private Long id;

    @NotEmpty(message = "Invalid name, can not be empty")
    private String name;
}
