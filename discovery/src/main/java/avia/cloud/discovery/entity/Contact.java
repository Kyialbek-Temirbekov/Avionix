package avia.cloud.discovery.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
public class Contact {
    @Id
    @UuidGenerator
    private String id;
    private String name;
    private String email;
    private String message;
    private LocalDate createdAt;
}
