package avia.cloud.flight.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
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
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String id;
    @ManyToOne
    @JoinColumn(name = "departure_flight_id", referencedColumnName = "id")
    @JsonIgnore
    private Flight departureFlight;
    @ManyToOne
    @JoinColumn(name = "return_flight_id", referencedColumnName = "id")
    @JsonIgnore
    private Flight returnFlight;
    @Pattern(regexp = "^[A-Z]{3}$", message = "Invalid IATA code")
    private String takeoffIata;
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime takeoffAt;
    @Pattern(regexp = "^[A-Z]{3}$", message = "Invalid IATA code")
    private String arrivalIata;
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime arrivalAt;
}
