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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "airplane_id", referencedColumnName = "id")
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
    @OneToMany(mappedBy = "flight")
    private List<Ticket> tickets;
    @OneToOne
    @JoinColumn(name = "article_id", referencedColumnName = "id")
    private Article article;
    @OneToOne
    @JoinColumn(name = "special_deal_id", referencedColumnName = "id")
    private SpecialDeal specialDeal;
}
