package avia.cloud.flight.service;

import avia.cloud.flight.dto.FlightDTO;
import avia.cloud.flight.entity.Flight;
import avia.cloud.flight.entity.enums.Cabin;
import avia.cloud.flight.entity.enums.Currency;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

public interface IFlightService {
    FlightDTO convertToFlightDTO(Flight flight, String lan);
    HashMap<String, Object> findPlaneSeatDetails(String flightId);
    HashMap<String, Object> searchFlights(String origin, String destination, boolean oneWay, LocalDate departureDate, LocalDate returnDate, int adults, List<Cabin> cabins, Currency currency, double minPrice, double maxPrice, Integer stops, Boolean checkedBaggageIncluded, Boolean cabinBaggageIncluded, long minFlightDuration, long maxFlightDuration, long minTransitDuration, long maxTransitDuration, String iata, int page, int pageSize, String direction, String property, String lan, String url);
}
