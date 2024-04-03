package avia.cloud.flight.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Segment {
    @Id
    @UuidGenerator
    private String id;
    @ManyToOne
    @JoinColumn(name = "departure_flight_id", referencedColumnName = "id")
    @JsonIgnore
    private Flight departureFlight;
    @ManyToOne
    @JoinColumn(name = "return_flight_id", referencedColumnName = "id")
    @JsonIgnore
    private Flight returnFlight;
    private String takeoffIata;
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime takeoffAt;
    private String arrivalIata;
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime arrivalAt;
}
