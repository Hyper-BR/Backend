package br.com.hyper.dtos.responses;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class PlaylistResponseDTO {
    private UUID id;
    private String name;
    private String coverUrl;
    private String description;
    private List<TrackResponseDTO> tracks;
}
