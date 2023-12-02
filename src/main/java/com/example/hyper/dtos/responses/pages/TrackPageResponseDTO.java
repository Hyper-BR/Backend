package com.example.hyper.dtos.responses.pages;

import com.example.hyper.dtos.requests.TrackRequestDTO;
import com.example.hyper.dtos.responses.TrackResponseDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class TrackPageResponseDTO {

    private boolean last;

    private boolean first;

    private Integer totalElements;

    private Integer totalPages;

    private Integer size;

    private Integer numberOfElements;

    @JsonProperty("page")
    private Integer number;

    @JsonProperty("musicDataList")
    private List<TrackResponseDTO> content;
}
