package avia.cloud.flight.repository;

import avia.cloud.flight.dto.PlaneSeatDetail;
import avia.cloud.flight.entity.Flight;
import avia.cloud.flight.entity.enums.Cabin;
import avia.cloud.flight.entity.enums.Currency;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface FlightRepository extends JpaRepository<Flight,String> {
    @Query("SELECT f FROM Flight f JOIN f.origin.names o JOIN f.destination.names d WHERE f.status = 'READY' AND (LOWER(o.name) LIKE LOWER(concat('%',:text,'%')) OR LOWER(d.name) LIKE LOWER(concat('%',:text,'%'))) OR f.airlineId IN :airlineIds")
    List<Flight> findAllByText(String text, List<String> airlineIds, Pageable pageable);
    List<Flight> findByAirlineId(String customerId);
    Optional<Flight> findTopByAirlineIdAndDestinationCode(String airlineId, String destination);
    @Query("SELECT new avia.cloud.flight.dto.PlaneSeatDetail(a.make, a.model, c.cabin, c.seatRow, c.seatCol) FROM Flight f JOIN Airplane a ON f.airplane = a JOIN Class c ON c.airplane = a WHERE f.id = :flightId")
    Optional<PlaneSeatDetail> findPlaneSeatDetails(String flightId);
    @Query("SELECT f FROM Flight f JOIN Segment s ON f = s.departureFlight JOIN Tariff t ON f = t.flight JOIN Class c ON f.airplane = c.airplane WHERE f.status = 'READY' AND f.origin.code = :origin AND f.destination.code = :destination AND f.oneWay = :oneWay AND CAST(s.takeoffAt as date) = :departureDate AND s.takeoffAt = (SELECT MIN(ds.takeoffAt) FROM Flight df JOIN df.departureSegment ds WHERE df.id = f.id) AND t.cabin IN :cabins AND (:currency IS NULL OR f.currency = :currency) AND t.price BETWEEN :minPrice AND :maxPrice AND (:stops IS NULL OR :stops + 1 = (SELECT COUNT(cs.id) FROM Segment cs WHERE cs.departureFlight = f)) AND (:checkedBaggageIncluded IS NULL OR t.checkedBaggageIncluded = :checkedBaggageIncluded) AND (:cabinBaggageIncluded IS NULL OR t.cabinBaggageIncluded = :cabinBaggageIncluded) AND f.departureFlightDuration BETWEEN :minFlightDuration AND :maxFlightDuration AND f.departureTransitDuration BETWEEN :minTransitDuration AND :maxTransitDuration AND (:airlineId IS NULL OR f.airlineId = :airlineId) AND c.seatCol * c.seatRow - SIZE(f.tickets) >= :adults")
    Page<Flight> searchFlights(String origin, String destination, boolean oneWay, LocalDate departureDate, int adults, List<Cabin> cabins, Currency currency, double minPrice, double maxPrice, Integer stops, Boolean checkedBaggageIncluded, Boolean cabinBaggageIncluded, long minFlightDuration, long maxFlightDuration, long minTransitDuration, long maxTransitDuration, String airlineId, Pageable pageable);
    /**
     * origin >
     * destination >
     * oneWay >
     * date >
     * adults ?
     *
     * cabin all
     * baggage all >
     * stop default
     * currency all >
     * price default >
     * flight duration default >
     * transit duration default >
     * airline all ?
     */
}
