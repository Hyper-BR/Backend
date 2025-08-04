package br.com.hyper.dtos.responses;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrackResponseDTO {

    private UUID id;
    private String title;
    private int duration;
    private String bpm;
    private String key;
    private String genre;
    private String fileUrl;
    private BigDecimal price;
    private Long plays;
    private Long purchases;
    private Long downloads;
    private String privacy;
    private String coverUrl;
    private List<ArtistResponseDTO> artists;
    private String createdDate;
}
