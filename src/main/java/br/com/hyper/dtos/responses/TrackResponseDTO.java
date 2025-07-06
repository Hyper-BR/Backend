package br.com.hyper.dtos.responses;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
public class TrackResponseDTO {

    private UUID id;
    private String title;
    private Double duration;
    private String genre;
    private String fileUrl;
    private BigDecimal price;
    private List<ArtistResponseDTO> artists;
}
