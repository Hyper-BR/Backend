package br.com.hyper.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "TRACK_PURCHASES")
public class TrackPurchaseEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ARTIST_ID", nullable = false)
    private ArtistEntity artist;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TRACK_ID", nullable = false)
    private TrackEntity track;

    @Column(name = "AMOUNT", nullable = false)
    private BigDecimal amount;

    @Column(name = "PAYMENT_ID", nullable = false, unique = true)
    private String paymentId;
}
