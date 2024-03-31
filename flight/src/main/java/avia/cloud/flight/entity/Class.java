package avia.cloud.flight.entity;

import avia.cloud.flight.entity.enums.Cabin;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Class {
    @Id
    @UuidGenerator
    private String id;
    @ManyToOne
    @JoinColumn(name = "airplane_id", referencedColumnName = "id")
    private Airplane airplane;
    @Enumerated(EnumType.STRING)
    private Cabin cabin;
    private short seatRow;
    private short seatCol;
}
