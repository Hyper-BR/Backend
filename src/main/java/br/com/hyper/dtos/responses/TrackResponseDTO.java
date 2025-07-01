package br.com.hyper.dtos.responses;

import lombok.Data;

@Data
public class TrackResponseDTO {

    private Long id;
    private String name;
    private Double duration;
    private String genre;
    private String image;
    private int price;
    private String path;
    private ArtistResponseDTO artist;
}
