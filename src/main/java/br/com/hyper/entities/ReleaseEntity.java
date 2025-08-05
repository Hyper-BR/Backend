package br.com.hyper.entities;

import br.com.hyper.enums.ReleaseStatus;
import br.com.hyper.enums.ReleaseType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "RELEASES")
@Builder
public class ReleaseEntity extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid")
    @JdbcTypeCode(SqlTypes.UUID)
    @Column(name = "ID", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "OWNER", nullable = false)
    private UUID owner;

    @Column(name = "TITLE", nullable = false)
    private String title;

    @Column(name = "TYPE", nullable = false)
    @Enumerated(EnumType.STRING)
    private ReleaseType type;

    @Column(name = "COVER_URL", nullable = false)
    private String coverUrl;

    @Column(name = "UPC", nullable = false)
    private String upc;

    @Column(name = "RELEASE_DATE", nullable = false)
    private ZonedDateTime releaseDate;

    @Column(name = "STATUS", nullable = false)
    @Enumerated(EnumType.STRING)
    private ReleaseStatus status;

    @Column(name = "DESCRIPTION", nullable = false)
    private String description;

    @OneToMany(mappedBy = "release", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TrackEntity> tracks;
}