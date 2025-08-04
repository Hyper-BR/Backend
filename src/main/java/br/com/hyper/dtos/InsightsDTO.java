package br.com.hyper.dtos;

import br.com.hyper.dtos.responses.TrackResponseDTO;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Data
@Setter
@Getter
@Builder
public class InsightsDTO {

    private Long totalPlays;
    private Long totalPurchases;
    private Long totalDownloads;
    private BigDecimal totalRevenue;
    private Integer totalTracks;
    private Long totalAlbums;
    private List<TrackResponseDTO> tracks;
    private List<ListenerDTO> topListeners;
}
