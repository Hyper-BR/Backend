package br.com.hyper.dtos.requests;

import br.com.hyper.enums.ReleaseType;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class ReleaseRequestDTO {

    private MultipartFile cover;
    private String description;
    private List<TrackRequestDTO> tracks;
    private ReleaseType type;
}
