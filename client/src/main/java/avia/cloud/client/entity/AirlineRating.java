package avia.cloud.client.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AirlineRating {
    public AirlineRating(Airline airline, short grade) {
        this.airline = airline;
        this.grade = grade;
    }
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "avia.cloud.client.util.CustomUuidGenerator")
    private String id;
    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "base_id")
    private Customer customer;
    @ManyToOne
    @JoinColumn(name = "airline_id", referencedColumnName = "base_id")
    private Airline airline;
    private short grade;
    private String feedback;
}
