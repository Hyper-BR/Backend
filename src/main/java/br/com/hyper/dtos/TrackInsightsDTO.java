package br.com.hyper.dtos;

import lombok.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.UUID;

@Data
@Setter
@Getter
@AllArgsConstructor
public class TrackInsightsDTO {

    private UUID id;
    private String title;
    private BigInteger plays;
    private BigInteger purchases;
    private BigInteger downloads;
    private BigDecimal revenue;
}
