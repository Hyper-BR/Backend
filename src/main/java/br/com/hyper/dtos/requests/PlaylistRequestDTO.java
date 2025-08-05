package br.com.hyper.dtos.requests;

import br.com.hyper.enums.Privacy;
import lombok.Data;

@Data
public class PlaylistRequestDTO {

    private String name;
    private Privacy privacy;
    private String description;
}
