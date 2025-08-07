package br.com.hyper.entities;

import br.com.hyper.enums.SubscriptionType;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE", nullable = false, unique = true)
    private SubscriptionType type;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "PAYMENT_ID", nullable = false)
    private String paymentId;

    @Column(name = "PRICE", nullable = false)
    private BigDecimal price;

}