package br.com.hyper.services;

import br.com.hyper.dtos.AudioDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import java.io.*;

@Service
@RequiredArgsConstructor
public class AudioStreamingServiceImpl implements AudioStreamingService {

    public AudioDTO buildStreamResponse(Resource audio, HttpServletRequest request) throws IOException {
        File file = audio.getFile();
        long total = file.length();
        String range = request.getHeader("Range");

        long start = 0;
        long end = total - 1;

        if (range != null) {
            String[] parts = range.replace("bytes=", "").split("-");
            start = Long.parseLong(parts[0]);
            if (parts.length > 1 && !parts[1].isEmpty()) {
                end = Long.parseLong(parts[1]);
            }
        }

        InputStream inputStream = new FileInputStream(file);
        inputStream.skip(start);
        MediaType contentType = MediaTypeFactory.getMediaType(file.getName())
                .orElse(MediaType.APPLICATION_OCTET_STREAM);

        return new AudioDTO(inputStream, start, end, total, contentType, file.getName());
    }
}
