package br.com.hyper.entities;

import br.com.hyper.constants.DistribuitionStatus;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ROYALTY_DISTRIBUTIONS")
@Builder
public class RoyaltyDistributionEntity extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "RECIPIENT_ID", nullable = false)
    private CustomerEntity customer;

    @Column(name = "ROLE")
    private String role;

    @Column(name = "AMOUNT_USD")
    private BigDecimal amountUsd;
}