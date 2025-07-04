package br.com.hyper.services;

import br.com.hyper.dtos.AudioDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.Resource;

import java.io.IOException;

public interface AudioStreamingService {

    AudioDTO buildStreamResponse(Resource audio, HttpServletRequest request) throws IOException;
}
