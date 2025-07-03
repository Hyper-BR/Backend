package br.com.hyper.dtos.responses;

import lombok.Data;

import java.util.UUID;

@Data
public class ArtistResponseDTO {

    private UUID id;

    private String name;
}
