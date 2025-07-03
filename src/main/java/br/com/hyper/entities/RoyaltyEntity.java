package br.com.hyper.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ROYALTIES")
@Builder
public class RoyaltyEntity extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "TRACK_ID", nullable = false)
    private TrackEntity track;

    @Column(name = "SOURCE_DSP", nullable = false)
    private String sourceDsp;

    @Column(name = "COUNTRY")
    private String country;

    @Column(name = "STREAM_COUNT")
    private Integer streamCount;

    @Column(name = "RENUE_USD")
    private BigDecimal revenueUsd;

    @Column(name = "REPORT_DATE", nullable = false)
    private ZonedDateTime reportDate;
}