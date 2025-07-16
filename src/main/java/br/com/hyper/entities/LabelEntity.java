package br.com.hyper.entities;

import br.com.hyper.enums.Privacy;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "LABELS")
@Builder
public class LabelEntity extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid")
    @JdbcTypeCode(SqlTypes.UUID)
    @Column(name = "ID", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "ARTIST_ID", nullable = false)
    private ArtistEntity artist;

    @Column(name = "USERNAME", nullable = false)
    private String username;

    @Column(name = "IS_VERIFIED", nullable = false)
    private boolean isVerified;
}