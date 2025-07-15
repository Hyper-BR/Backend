package br.com.hyper.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class ArtistEntity extends CustomerEntity implements Serializable {

    @Column(name = "USERNAME", nullable = false, unique = true)
    private String username;

    @Column(name = "IS_VERIFIED", nullable = false)
    private Boolean isVerified;

}