package avia.cloud.flight.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.List;

import static jakarta.persistence.CascadeType.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class City {
    @Id
    private String code;
    private String countryCode;
    @OneToMany(mappedBy = "city", fetch = FetchType.EAGER, cascade = {PERSIST,REMOVE,MERGE})
    private List<CityNames> names;
    @OneToMany(mappedBy = "origin")
    @JsonIgnore
    private List<Flight> originFlights;
    @OneToMany(mappedBy = "destination")
    @JsonIgnore
    private List<Flight> destinationFlights;
}
