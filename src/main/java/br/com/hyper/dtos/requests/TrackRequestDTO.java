package br.com.hyper.dtos.requests;

import br.com.hyper.enums.Privacy;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Data
public class TrackRequestDTO {

    private UUID id;
    private String title;
    private String genre;
    private MultipartFile file;
    private Privacy privacy;
    private List<String> tags;
    private List<ArtistRequestDTO> artists;
}
