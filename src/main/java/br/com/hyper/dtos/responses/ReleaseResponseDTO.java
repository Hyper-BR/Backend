package br.com.hyper.dtos.responses;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class ReleaseResponseDTO {

    private UUID id;
    private String title;
    private String type;
    private String coverUrl;
    private String upc;
    private String releaseDate;
    private String status;
    private List<TrackResponseDTO> tracks;
}
