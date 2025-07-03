package br.com.hyper.dtos.responses;

import lombok.Data;

import java.util.UUID;

@Data
public class TrackResponseDTO {

    private UUID id;
    private String name;
    private Double duration;
    private String genre;
    private String image;
    private int price;
    private String path;
    private ArtistResponseDTO artist;
}
