package br.com.hyper.dtos;

import br.com.hyper.dtos.responses.TrackResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReleaseInsightsDTO {
    private UUID id;
    private String title;
    private String coverUrl;
    private String type;
    private Long totalPlays;
    private Long totalPurchases;
    private Long totalDownloads;
    private BigDecimal totalRevenue;
    private List<TrackResponseDTO> tracks;
}
