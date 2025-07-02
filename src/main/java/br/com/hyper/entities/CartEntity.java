package br.com.hyper.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "CART")
@EqualsAndHashCode(callSuper = false)
public class CartEntity extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CART_SEQ")
    @SequenceGenerator(name = "CART_SEQ", sequenceName = "CART_SEQ", allocationSize = 1)
    private Long id;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "TOTAL_ITEMS", nullable = false)
    private int totalItems;

    @Column(name = "TOTAL_PRICE", nullable = false)
    private BigDecimal totalPrice;

    @ManyToOne
    @JoinColumn(name = "ARTIST", nullable = false)
    private ArtistEntity artist;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "CART_TRACK",
            joinColumns = @JoinColumn(name = "CART_ID"),
            inverseJoinColumns = @JoinColumn(name = "TRACK_ID")
    )
    private List<ReleaseEntity> tracks;
}