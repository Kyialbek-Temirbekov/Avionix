package avia.cloud.flight.service;

import avia.cloud.flight.dto.FlightDTO;
import avia.cloud.flight.dto.FlightRequestDTO;
import avia.cloud.flight.entity.Flight;
import avia.cloud.flight.entity.Ticket;
import avia.cloud.flight.entity.enums.Cabin;
import avia.cloud.flight.entity.enums.Currency;
import avia.cloud.flight.entity.enums.FlightStatus;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface IFlightService {
    void createFlight(FlightRequestDTO flight, String token);
    void updateStatus(String flightId, FlightStatus status);
    FlightDTO convertToFlightDTO(Flight flight, String lan);
    HashMap<String, Object> findPlaneSeatDetails(String flightId);
    HashMap<String, Object> searchFlights(String origin, String destination, boolean oneWay, LocalDate departureDate, LocalDate returnDate, int adults, List<Cabin> cabins, Currency currency, double minPrice, double maxPrice, Integer stops, Boolean checkedBaggageIncluded, Boolean cabinBaggageIncluded, long minFlightDuration, long maxFlightDuration, long minTransitDuration, long maxTransitDuration, String airlineId, int page, int pageSize, String direction, String property, String lan, String url);
    HashMap<String,Object> fetchOwnerFlights(String token, String lan, int page, int pageSize,String url);
    List<FlightDTO> fetchFlights(String text, String lan);
    FlightDTO fetchFlight(String flightId, String lan);
    Map<String,Object> globalSearch(String text, String lan);
}
