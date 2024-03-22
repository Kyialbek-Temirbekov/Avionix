package avia.cloud.flight.entity;

import avia.cloud.flight.entity.enums.Lan;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Data
@NoArgsConstructor
public class CityNames {
    @Id
    @UuidGenerator
    private String id;
    @ManyToOne
    @JoinColumn(name = "city_code", referencedColumnName = "code")
    @JsonIgnore
    private City city;
    private String name;
    @Enumerated(EnumType.STRING)
    private Lan lan;
}
