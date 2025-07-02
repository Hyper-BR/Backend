package br.com.hyper.dtos.responses;

import br.com.hyper.entities.ReleaseEntity;
import lombok.Data;

@Data
public class ReviewResponseDTO {
    private Long id;

    private float score;

    private ReleaseEntity trackId;
}
