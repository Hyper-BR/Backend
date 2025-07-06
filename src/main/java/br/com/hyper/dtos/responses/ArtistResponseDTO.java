package br.com.hyper.dtos.responses;

import lombok.*;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class ArtistResponseDTO {

    private UUID id;

    private String username;
}
