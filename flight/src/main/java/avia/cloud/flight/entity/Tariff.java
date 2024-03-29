package avia.cloud.flight.entity;

import avia.cloud.flight.entity.enums.Cabin;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class Tariff {
    @Id
    @UuidGenerator
    private String id;
    @OneToOne
    @JoinColumn(name = "flight_id", referencedColumnName = "id")
    @JsonIgnore
    private Flight flight;
    @Enumerated(EnumType.STRING)
    private Cabin cabin;
    private double price;
    private double baggagePrice;
    private short discount;
    private boolean checkedBaggageIncluded;
    private boolean cabinBaggageIncluded;
}
