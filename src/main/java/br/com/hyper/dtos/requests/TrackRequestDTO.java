package br.com.hyper.dtos.requests;

import lombok.Data;

import java.util.UUID;

@Data
public class TrackRequestDTO {

    private UUID id;
    private String title;
    private String genre;
    private boolean isExplicit;
    private String language;
    private String fileUrl;
}
