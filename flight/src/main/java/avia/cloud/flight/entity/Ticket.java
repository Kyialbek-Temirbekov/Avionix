package avia.cloud.flight.entity;

import avia.cloud.flight.entity.enums.TicketStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class Ticket extends BaseEntity {
    @Id
    @UuidGenerator
    private String id;
    @ManyToOne
    @JoinColumn(name = "flight_id", referencedColumnName = "id")
    private Flight flight;
    private String customerId;
    private String seat;
    private boolean checkedBaggageIncluded;
    private double price;
    @Enumerated(EnumType.STRING)
    private TicketStatus status;
}
