package br.com.hyper.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ARTISTS")
public class ArtistEntity extends BaseEntity implements Serializable {

    @Id
    @JdbcTypeCode(SqlTypes.UUID)
    @Column(name = "ID", updatable = false, nullable = false)
    private UUID id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "ID")
    private CustomerEntity customer;

    @Column(name = "USERNAME", nullable = false, unique = true)
    private String username;

    @Column(name = "FREE_TRACK_LIMIT", nullable = false)
    private Integer freeTrackLimit;

    @Column(name = "IS_VERIFIED", nullable = false)
    private Boolean isVerified;

    @JsonProperty("avatarUrl")
    public String getAvatar() {
        return customer.getAvatarUrl();
    }

}