package avia.cloud.flight.repository;

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

@Repository
public interface FlightRepository extends JpaRepository<Flight,String> {
    @Query("SELECT f FROM Flight f JOIN Segment s ON f = s.flight JOIN Tariff t ON f = t.flight WHERE f.status = 'READY' AND f.origin.code = :origin AND f.destination.code = :destination AND f.oneWay = :oneWay AND CAST(s.departureAt as date) = :date AND s.departureAt = (SELECT MIN(ds.departureAt) FROM Flight df JOIN df.segments ds WHERE df.id = f.id) AND (:cabin IS NULL OR t.cabin = :cabin) AND (:currency IS NULL OR f.currency = :currency) AND t.price BETWEEN :minPrice AND :maxPrice AND (:stops IS NULL OR :stops + 1 = (SELECT COUNT(cs.id) FROM Segment cs WHERE cs.flight = f)) AND (:checkedBaggageIncluded IS NULL OR t.checkedBaggageIncluded = :checkedBaggageIncluded) AND (:cabinBaggageIncluded IS NULL OR t.cabinBaggageIncluded = :cabinBaggageIncluded) AND f.flightDuration BETWEEN :minFlightDuration AND :maxFlightDuration AND f.transitDuration BETWEEN :minTransitDuration AND :maxTransitDuration AND (:iata IS NULL OR f.iata = :iata)")
    Page<Flight> searchFlights(String origin, String destination, boolean oneWay, LocalDate date, Cabin cabin, Currency currency, double minPrice, double maxPrice, Integer stops, Boolean checkedBaggageIncluded, Boolean cabinBaggageIncluded, long minFlightDuration, long maxFlightDuration, long minTransitDuration, long maxTransitDuration, String iata, Pageable pageable);
    /**
     * origin >
     * destination >
     * oneWay >
     * date >
     * adults ?
     *
     * cabin all >
     * baggage all >
     * stop default >
     * currency all >
     * price default >
     * flight duration default >
     * transit duration default >
     * airline all
     */
}
