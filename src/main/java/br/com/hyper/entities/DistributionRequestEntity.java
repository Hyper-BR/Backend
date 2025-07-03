package br.com.hyper.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.ZonedDateTime;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "DISTRIBUTION_REQUESTS")
@Builder
public class DistributionRequestEntity extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "RELEASE_ID", nullable = false)
    private ReleaseEntity release;

    @Column(name = "TARGET_DSPS", columnDefinition = "jsonb", nullable = false)
    private String targetDsps;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "SUBMITTED_AT")
    private ZonedDateTime submittedAt;

    @Column(name = "DELIVERED_AT")
    private ZonedDateTime deliveredAt;
}