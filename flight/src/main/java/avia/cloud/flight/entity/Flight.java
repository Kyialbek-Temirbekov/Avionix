package avia.cloud.flight.entity;

import avia.cloud.flight.entity.enums.Currency;
import avia.cloud.flight.entity.enums.FlightStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.*;

import java.util.List;

import static jakarta.persistence.CascadeType.*;

@Entity
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class Flight extends BaseEntity {
    @Id
    @UuidGenerator
    private String id;
    private String iata;
    @OneToMany(mappedBy = "flight", fetch = FetchType.EAGER, cascade = {REMOVE,PERSIST,MERGE})
    private List<Segment> segments;
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
    private long flightDuration;
    private long transitDuration;
}
