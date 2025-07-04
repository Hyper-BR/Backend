package br.com.hyper.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.MediaType;

import java.io.InputStream;

@Data
@AllArgsConstructor
public class AudioDTO {
    private InputStream stream;
    private long start;
    private long end;
    private long total;
    private MediaType contentType;
    private String filename;
}
