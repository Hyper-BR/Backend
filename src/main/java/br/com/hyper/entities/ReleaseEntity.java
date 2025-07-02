package br.com.hyper.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Data
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "RELEASE")
@Builder
@EqualsAndHashCode(callSuper = false)
public class ReleaseEntity extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TRACK_SEQ")
    @SequenceGenerator(name = "TRACK_SEQ", sequenceName = "TRACK_SEQ", allocationSize = 1)
    private Long id;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "DURATION", nullable = false)
    private float size;

    @Column(name = "GENRE", nullable = false)
    private String genre;

    @Column(name = "PRICE", nullable = false)
    private int price;

    @Column(name = "IMAGE", nullable = false)
    private String image;

    @Column(name = "SPOTIFY_ID")
    private Long spotifyId;

    @Column(name = "ITUNES_ID")
    private Long itunesId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ARTIST", nullable = false)
    private ArtistEntity artist;

    @Column(name = "PATH", nullable = false)
    private String path;
}