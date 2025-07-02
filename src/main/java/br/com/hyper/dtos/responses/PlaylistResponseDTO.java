package br.com.hyper.dtos.responses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlaylistResponseDTO {
    private Long id;
    private String name;
    private String description;
    private String image;
}
