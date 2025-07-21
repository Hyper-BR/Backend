package br.com.hyper.dtos.responses;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@Getter
@Setter
public class TrackResponseDTO {

    private UUID id;
    private String title;
    private int duration;
    private String bpm;
    private String key;
    private String genre;
    private String fileUrl;
    private BigDecimal price;
    private String coverUrl;
    private List<ArtistResponseDTO> artists;
    private String createdDate;
}
