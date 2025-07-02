package br.com.hyper.entities;

import br.com.hyper.enums.StatusEmail;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "EMAIL")
public class EmailEntity extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid")
    @JdbcTypeCode(SqlTypes.UUID)
    @Column(name = "ID", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "OWNER", nullable = false)
    private String owner;

    @Column(name = "EMAIL_FROM", nullable = false)
    private String emailFrom;

    @Column(name = "SUBJECT", nullable = false)
    private String subject;

    @Column(name = "EMAIL_TO", nullable = false)
    private String emailTo;

    @Column(name = "TEXT", nullable = false)
    private String text;

    @Column(name = "SEND_DATE", nullable = false)
    private ZonedDateTime sendDate;

    @Enumerated(EnumType.STRING)
    @JoinColumn(name = "STATUS", nullable = false)
    private StatusEmail status;
}