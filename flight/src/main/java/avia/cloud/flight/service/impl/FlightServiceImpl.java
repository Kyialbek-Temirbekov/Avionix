package avia.cloud.flight.service.impl;

import avia.cloud.flight.dto.FlightDTO;
import avia.cloud.flight.entity.Flight;
import avia.cloud.flight.entity.Segment;
import avia.cloud.flight.entity.enums.Cabin;
import avia.cloud.flight.entity.enums.Currency;
import avia.cloud.flight.repository.FlightRepository;
import avia.cloud.flight.service.IFlightService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class FlightServiceImpl implements IFlightService {
    private final FlightRepository flightRepository;
    private final ModelMapper modelMapper;
    @Override
    public List<FlightDTO> searchFlights(String origin, String destination, boolean oneWay, LocalDate date, Cabin cabin, Currency currency, double minPrice, double maxPrice, Integer stops, Boolean checkedBaggageIncluded, Boolean cabinBaggageIncluded, long minFlightDuration, long maxFlightDuration, long minTransitDuration, long maxTransitDuration) {
        return flightRepository.searchFlights(origin,destination,oneWay,date, cabin, currency, minPrice, maxPrice, stops, checkedBaggageIncluded, cabinBaggageIncluded, minFlightDuration, maxFlightDuration, minTransitDuration, maxTransitDuration).stream().map(this::convertToFlightDTO).toList();
    }

    private FlightDTO convertToFlightDTO(Flight flight) {
        FlightDTO flightDTO = modelMapper.map(flight, FlightDTO.class);
        flightDTO.setFrom(flight.getOrigin().getCode());
        flightDTO.setTo(flight.getDestination().getCode());
        return flightDTO;
    }

    public long calculateFlightDuration(List<Segment> segments) {
        LocalDateTime firstDeparture = segments.get(0).getDepartureAt();
        LocalDateTime lastArrival = segments.get(segments.size() - 1).getArrivalAt();
        return ChronoUnit.MINUTES.between(firstDeparture, lastArrival);
    }

    public long calculateTransitDuration(List<Segment> segments) {
        if (segments.size() <= 1) {
            return 0;
        }
        long transitDuration = 0;
        for (int i = 0; i < segments.size() - 1; i++) {
            LocalDateTime currentArrival = segments.get(i).getArrivalAt();
            LocalDateTime nextDeparture = segments.get(i + 1).getDepartureAt();
            transitDuration += ChronoUnit.MINUTES.between(currentArrival, nextDeparture);
        }
        return transitDuration;
    }
}
