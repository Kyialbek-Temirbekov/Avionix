package avia.cloud.flight.repository;

import avia.cloud.flight.entity.Flight;
import avia.cloud.flight.entity.enums.Cabin;
import avia.cloud.flight.entity.enums.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface FlightRepository extends JpaRepository<Flight,String> {
    @Query("SELECT f FROM Flight f JOIN Segment s ON f = s.flight JOIN Tariff t ON f = t.flight WHERE f.origin.code = :origin AND f.destination.code = :destination AND f.oneWay = :oneWay AND CAST(s.departureAt as date) = :date AND s.departureAt = (SELECT MIN(ds.departureAt) FROM Flight df JOIN df.segments ds WHERE df.id = f.id) AND (:cabin IS NULL OR t.cabin = :cabin) AND (:currency IS NULL OR f.currency = :currency) AND t.price BETWEEN :minPrice AND :maxPrice")
    List<Flight> searchFlights(String origin, String destination, boolean oneWay, LocalDate date, Cabin cabin, Currency currency, double minPrice, double maxPrice);
    /**
     * origin >
     * destination >
     * oneWay >
     * date >
     * adults
     *
     * cabin all >
     * baggage all
     * stop default
     * currency all >
     * price default >
     * flight duration default
     * transit duration default
     */
}
