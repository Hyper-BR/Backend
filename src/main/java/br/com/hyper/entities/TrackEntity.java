package br.com.hyper.entities;

import br.com.hyper.enums.Privacy;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TRACKS")
@Builder
public class TrackEntity extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid")
    @JdbcTypeCode(SqlTypes.UUID)
    @Column(name = "ID", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "TITLE", nullable = false)
    private String title;

    @Column(name = "FILE_URL", nullable = false)
    private String fileUrl;

    @Column(name = "DURATION", nullable = false)
    private Integer durationInSeconds;

    @Column(name = "GENRE", nullable = false)
    private String genre;

    @Column(name = "PRICE", nullable = false)
    private BigDecimal price;

    @Column(name = "TAGS", nullable = false)
    private List<String> tags;

    @Column(name = "ISRC", nullable = false)
    private String isrc;

    @Column(name = "PLAYS", nullable = false)
    private BigInteger plays;

    @ManyToOne
    @JoinColumn(name = "RELEASE_ID", nullable = false)
    private ReleaseEntity release;

    @ManyToMany
    @JoinTable(
            name = "track_artists",
            joinColumns = @JoinColumn(name = "track_id"),
            inverseJoinColumns = @JoinColumn(name = "artist_id")
    )
    private List<ArtistEntity> artists;

    @Column(name = "PRIVACY", nullable = false)
    private Privacy privacy;

}