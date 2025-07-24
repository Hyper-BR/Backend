package br.com.hyper.dtos;

import br.com.hyper.dtos.responses.CustomerResponseDTO;
import lombok.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

@Data
@Setter
@Getter
public class InsightsDTO {

    private BigInteger totalPlays;
    private BigDecimal totalRevenue;
    private long totalTracks;
    private long totalAlbums;
    private List<CustomerResponseDTO> topListeners;
}
