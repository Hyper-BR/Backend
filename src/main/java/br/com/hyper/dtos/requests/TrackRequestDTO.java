package br.com.hyper.dtos.requests;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;

@Data
public class TrackRequestDTO {

    private Long id;

    @NotEmpty(message = "Invalid name, can not be empty")
    private String name;

    @NotEmpty(message = "Invalid genre, can not be empty")
    private String genre;

    @NotEmpty(message = "Invalid image, can not be empty")
    private String image;

    @NotEmpty(message = "Invalid image, can not be empty")
    private MultipartFile file;

}
