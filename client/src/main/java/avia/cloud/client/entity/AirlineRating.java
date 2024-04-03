package avia.cloud.client.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
public class AirlineRating {
    public AirlineRating(Airline airline, short grade) {
        this.airline = airline;
        this.grade = grade;
    }
    @Id
    @UuidGenerator
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
