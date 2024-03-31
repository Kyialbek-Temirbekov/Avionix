package avia.cloud.flight.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;

import static jakarta.persistence.CascadeType.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Airplane {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "avia.cloud.flight.util.CustomUuidGenerator")
    private String id;
    private String make;
    private String model;
    @OneToMany(mappedBy = "airplane", fetch = FetchType.EAGER, cascade = {PERSIST,REMOVE,MERGE})
    private List<Class> cabins;
    @OneToMany(mappedBy = "airplane")
    private List<Flight> flights;
}
