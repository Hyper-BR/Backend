package br.com.hyper.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Data
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "REVIEW")
@EqualsAndHashCode(callSuper = false)
public class ReviewEntity extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REVIEW_SEQ")
    @SequenceGenerator(name = "REVIEW_SEQ", sequenceName = "REVIEW_SEQ", allocationSize = 1)
    private Long id;

    @Column(name = "SCORE")
    private float score;

    @ManyToOne(fetch = FetchType.EAGER)
    private CustomerEntity customerId;

    @ManyToOne(fetch = FetchType.EAGER)
    private TrackEntity trackId;

}
