package br.com.hyper.dtos.requests;

import br.com.hyper.entities.ReleaseEntity;
import lombok.Data;

@Data
public class ReviewRequestDTO {

    private Long id;

    private float score;

    private ReleaseEntity trackId;
}