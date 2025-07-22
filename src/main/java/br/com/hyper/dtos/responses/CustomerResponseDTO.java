package br.com.hyper.dtos.responses;

import br.com.hyper.dtos.SubscriptionDTO;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

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
    private String biography;
    private String country;
    private String avatarUrl;
    private String coverUrl;
    private String followers;
    private String following;
    private ArtistResponseDTO artistProfile;
    private boolean isArtist;
    private boolean isLabel;
}
