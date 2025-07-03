package br.com.hyper.dtos.responses;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class PlaylistResponseDTO {
    private UUID id;
    private String name;
    private String description;
}
