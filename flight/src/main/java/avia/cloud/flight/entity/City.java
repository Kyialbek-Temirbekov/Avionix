package avia.cloud.flight.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.List;

import static jakarta.persistence.CascadeType.*;

@Entity
@Data
@NoArgsConstructor
public class City {
    @Id
    private String code;
    private String countryCode;
    @OneToMany(mappedBy = "city", fetch = FetchType.EAGER, cascade = {PERSIST,REMOVE,MERGE})
    private List<CityNames> names;
    @OneToMany(mappedBy = "origin")
    private List<Flight> originFlights;
    @OneToMany(mappedBy = "destination")
    private List<Flight> destinationFlights;
}
