package br.com.hyper.dtos.requests;

import br.com.hyper.enums.ReleaseType;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import java.util.UUID;

@Data
public class ReleaseRequestDTO {

    private UUID id;

    @NotEmpty(message = "Invalid title, can not be empty")
    private String title;

    @NotEmpty(message = "Invalid type, can not be empty")
    private ReleaseType type;

    @NotEmpty(message = "Invalid genre, can not be empty")
    private String genre;

    @NotEmpty(message = "Invalid image, can not be empty")
    private MultipartFile file;

}
