package br.com.hyper.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ARTISTS")
public class ArtistEntity extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ARTISTS_SEQ")
    @SequenceGenerator(name = "ARTISTS_SEQ", sequenceName = "ARTISTS_SEQ", allocationSize = 1)
    private Long id;

    @Column(name = "NAME", nullable = false, unique = true)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CUSTOMER", nullable = false)
    private CustomerEntity customer;

    @Column(name = "PROFILE_IMAGE")
    private String profileImage;

    @Column(name = "IS_VERIFIED", nullable = false)
    private Boolean isVerified;

    @OneToMany(mappedBy = "artist", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CartEntity> carts;

    @Column(name = "BIO")
    private String bio;
}