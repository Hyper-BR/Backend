package br.com.hyper.dtos.responses;

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
    private SubscriptionResponseDTO subscription;
    private String country;
    private String avatarUrl;
    private ArtistResponseDTO artistProfile;
}
