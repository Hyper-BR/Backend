package br.com.hyper.entities;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.ZonedDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
public class BaseEntity {
     @CreationTimestamp
     @Column(name = "CREATED_DATE")
     private ZonedDateTime createdDate;
     @UpdateTimestamp
     @Column(name = "LAST_MODIFIED_DATE")
     private ZonedDateTime lastModifiedDate;

     @PrePersist
     public void prePersist() {
          ZonedDateTime now = ZonedDateTime.now();
          this.createdDate = now;
          this.lastModifiedDate = now;
     }

     @PreUpdate
     public void preUpdate() {
          this.lastModifiedDate = ZonedDateTime.now();
     }
}
