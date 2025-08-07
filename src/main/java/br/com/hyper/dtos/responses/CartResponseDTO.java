package br.com.hyper.dtos.responses;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class CartResponseDTO {

    private UUID id;
    private String name;
    private List<TrackResponseDTO> tracks;
}
