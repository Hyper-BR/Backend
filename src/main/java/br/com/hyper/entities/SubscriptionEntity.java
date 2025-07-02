package br.com.hyper.entities;

import br.com.hyper.enums.SubscriptionOption;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "SUBSCRIPTIONS")
public class SubscriptionEntity extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SUBSCRIPTIONS_SEQ")
    @SequenceGenerator(name = "SUBSCRIPTIONS_SEQ", sequenceName = "SUBSCRIPTIONS_SEQ", allocationSize = 1)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "PLAN", nullable = false)
    private SubscriptionOption plan;

    @Column(name = "PRICE", nullable = false)
    private BigDecimal price;
}