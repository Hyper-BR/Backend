package br.com.hyper.dtos.responses;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class CustomerResponseDTO {

    private UUID id;
    private String name;
    private String email;
    private String birthDate;
    private SubscriptionResponseDTO subscription;
    private String country;
    private String avatar;
    private List<ArtistResponseDTO> artistProfiles;
}
