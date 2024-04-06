package avia.cloud.flight.repository;

import avia.cloud.flight.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket,String> {
    List<Ticket> findAllByCustomerId(String customerId);
    List<Ticket> findProjectedByFlightId(String flightId);
}
