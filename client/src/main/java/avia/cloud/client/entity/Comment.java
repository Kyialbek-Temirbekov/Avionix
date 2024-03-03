package avia.cloud.client.entity;

import avia.cloud.client.entity.enums.Lan;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
public class Comment {
    @Id
    @UuidGenerator
    private String id;
    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "base_id")
    private Customer customer;
    private String description;
    @CreatedDate
    @Column(updatable = false)
    private LocalDate createdAt;
    private int grade;
    private boolean checked;
    @Enumerated(EnumType.STRING)
    private Lan lan;
}
