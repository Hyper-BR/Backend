package br.com.hyper.dtos.requests;

import lombok.Data;

import java.util.UUID;

@Data
public class ArtistRequestDTO {

    private UUID id;

    private String name;

}
