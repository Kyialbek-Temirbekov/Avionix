package avia.cloud.client.entity;

import avia.cloud.client.entity.enums.Lan;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@Entity
public class Comment {
    @Id
    @UuidGenerator
    private String id;
    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "base_id")
    private Customer customer;
    private String description;
    private LocalDate createdAt;
    private int grade;
    private boolean checked;
    private Lan lan;
}
