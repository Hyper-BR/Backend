package br.com.hyper.dtos.responses;

import br.com.hyper.dtos.SubscriptionDTO;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Data
@Setter
@Getter
public class CustomerResponseDTO {

    private UUID id;
    private String name;
    private String email;
    private String birthDate;
    private SubscriptionDTO subscription;
    private String country;
    private String avatarUrl;
    private ArtistResponseDTO artistProfile;
    private boolean isArtist;
    private boolean isLabel;
}
