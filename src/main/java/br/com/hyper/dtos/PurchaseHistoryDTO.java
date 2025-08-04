package br.com.hyper.dtos;

import br.com.hyper.dtos.responses.ArtistResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Setter
@Getter
@AllArgsConstructor
public class PurchaseHistoryDTO {
    private UUID trackId;
    private String title;
    private BigDecimal price;
    private List<ArtistResponseDTO> artists;
    private ZonedDateTime createdDate;
}
