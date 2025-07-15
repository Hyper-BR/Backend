package br.com.hyper.dtos.requests;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class ReleaseRequestDTO {

    private MultipartFile image;
    private List<TrackRequestDTO> tracks;
    private String description;
}
