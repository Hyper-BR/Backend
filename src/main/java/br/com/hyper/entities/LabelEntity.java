package br.com.hyper.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "LABELS")
@Builder
public class LabelEntity extends BaseEntity implements Serializable {

    @Id
    @JdbcTypeCode(SqlTypes.UUID)
    @Column(name = "ID", updatable = false, nullable = false)
    private UUID id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "ID")
    private CustomerEntity customer;

    @Column(name = "USERNAME", nullable = false)
    private String username;

    @Column(name = "IS_VERIFIED", nullable = false)
    private boolean isVerified;
}