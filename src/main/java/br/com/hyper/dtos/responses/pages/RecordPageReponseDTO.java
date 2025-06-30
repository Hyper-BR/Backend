package br.com.hyper.dtos.responses.pages;

import br.com.hyper.dtos.responses.RecordResponseDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class RecordPageReponseDTO {
    private boolean last;

    private boolean first;

    private Integer totalElements;

    private Integer totalPages;

    private Integer size;

    private Integer numberOfElements;

    @JsonProperty("page")
    private Integer number;

    @JsonProperty("records")
    private List<RecordResponseDTO> content;
}
