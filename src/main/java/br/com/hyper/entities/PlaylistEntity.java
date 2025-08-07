package br.com.hyper.entities;

import br.com.hyper.enums.Privacy;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "PLAYLISTS")
public class PlaylistEntity extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid")
    @JdbcTypeCode(SqlTypes.UUID)
    @Column(name = "ID", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CUSTOMER_ID", nullable = false)
    private CustomerEntity customer;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "COVER_URL", nullable = false)
    private String coverUrl;

    @Column(name = "DESCRIPTION")
    private String description;

    @ManyToMany
    @JoinTable(
            name = "playlist_collaborators",
            joinColumns = @JoinColumn(name = "playlist_id"),
            inverseJoinColumns = @JoinColumn(name = "customer_id")
    )
    private List<CustomerEntity> collaborators;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "PLAYLIST_TRACK",
            joinColumns = @JoinColumn(name = "PLAYLIST_ID"),
            inverseJoinColumns = @JoinColumn(name = "TRACK_ID")
    )
    private List<TrackEntity> tracks;

    @Enumerated(EnumType.STRING)
    @Column(name = "PRIVACY", nullable = false)
    private Privacy privacy;
}
