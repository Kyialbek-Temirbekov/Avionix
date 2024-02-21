package avia.cloud.client.entity;

import avia.cloud.client.entity.enums.Lan;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@Entity
public class Comment {
    @Id
    private String id;
    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private Customer customer;
    private String description;
    private LocalDate createdAt;
    private int grade;
    private boolean checked;
    private Lan lan;
}
