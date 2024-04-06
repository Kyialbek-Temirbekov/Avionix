package avia.cloud.flight.entity;

import avia.cloud.flight.entity.enums.Cabin;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Tariff {
    @Id
    @UuidGenerator
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
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
