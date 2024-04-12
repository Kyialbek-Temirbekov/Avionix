package avia.cloud.flight.entity;

import avia.cloud.flight.entity.enums.Currency;
import avia.cloud.flight.entity.enums.FlightStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.*;

import java.util.List;

import static jakarta.persistence.CascadeType.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class Flight extends BaseEntity {
    @Id
    @UuidGenerator
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String id;
    private String airlineId;
    private String number;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "airplane_id", referencedColumnName = "id")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Airplane airplane;
    @OneToMany(mappedBy = "departureFlight", fetch = FetchType.EAGER, cascade = {REMOVE,PERSIST,MERGE})
    private List<Segment> departureSegment;
    @OneToMany(mappedBy = "returnFlight", fetch = FetchType.EAGER, cascade = {REMOVE,PERSIST,MERGE})
    private List<Segment> returnSegment;
    private boolean oneWay;
    @ManyToOne
    @JoinColumn(name = "origin", referencedColumnName = "code")
    private City origin;
    @ManyToOne
    @JoinColumn(name = "destination", referencedColumnName = "code")
    private City destination;
    private String gate;
    @OneToOne(mappedBy = "flight", cascade = {REMOVE,PERSIST,MERGE})
    private Tariff tariff;
    @Enumerated(EnumType.STRING)
    private Currency currency;
    @Enumerated(EnumType.STRING)
    private FlightStatus status;
    private long departureFlightDuration;
    private long departureTransitDuration;
    private long returnFlightDuration;
    private long returnTransitDuration;
    @JsonIgnore
    @OneToMany(mappedBy = "flight")
    private List<Ticket> tickets;
}
