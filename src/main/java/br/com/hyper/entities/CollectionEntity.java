package br.com.hyper.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Data
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "COLLECTION")
@EqualsAndHashCode(callSuper = false)
public class CollectionEntity extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COLLECTION_SEQ")
    @SequenceGenerator(name = "COLLECTION_SEQ", sequenceName = "COLLECTION_SEQ", allocationSize = 1)
    private Long id;

    @Column(name = "NAME", nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CUSTOMER", nullable = false)
    private CustomerEntity customer;

//    @OneToMany(fetch = FetchType.LAZY)
//    @JoinColumn(name = "PLAYLIST", nullable = false)
//    private List<PlaylistEntity> playlists;
}