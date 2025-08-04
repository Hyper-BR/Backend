package br.com.hyper.dtos;

import br.com.hyper.dtos.responses.CustomerResponseDTO;
import lombok.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

@Data
@Setter
@Getter
@Builder
public class InsightsDTO {

    private BigInteger totalPlays;
    private BigInteger totalPurchases;
    private BigInteger totalDownloads;
    private BigDecimal totalRevenue;
    private BigInteger totalTracks;
    private BigInteger totalAlbums;
    private List<TrackInsightsDTO> tracks;
    private List<CustomerResponseDTO> topListeners;
}
