package br.com.hyper.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.io.Serializable;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "DISTRIBUTION_REQUESTS")
@Builder
public class DistributionRequestEntity extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid")
    @JdbcTypeCode(SqlTypes.UUID)
    @Column(name = "ID", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "RELEASE_ID", nullable = false)
    private ReleaseEntity release;

    @Column(name = "target_dsps", columnDefinition = "jsonb")
    private String targetDsps;

    private String status;

    @Column(name = "submitted_at")
    private ZonedDateTime submittedAt;

    @Column(name = "delivered_at")
    private ZonedDateTime deliveredAt;
}