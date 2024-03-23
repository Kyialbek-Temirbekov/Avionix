package avia.cloud.flight.service;

import avia.cloud.flight.dto.FlightDTO;
import avia.cloud.flight.entity.enums.Cabin;
import avia.cloud.flight.entity.enums.Currency;

import java.time.LocalDate;
import java.util.List;

public interface IFlightService {
    List<FlightDTO> searchFlights(String origin, String destination, boolean oneWay, LocalDate date, Cabin cabin, Currency currency, double minPrice, double maxPrice, Integer stops, Boolean checkedBaggageIncluded, Boolean cabinBaggageIncluded, long minFlightDuration, long maxFlightDuration, long minTransitDuration, long maxTransitDuration);
}
