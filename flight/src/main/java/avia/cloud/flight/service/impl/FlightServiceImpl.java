package avia.cloud.flight.service.impl;

import avia.cloud.flight.dto.FlightDTO;
import avia.cloud.flight.entity.Flight;
import avia.cloud.flight.entity.enums.Cabin;
import avia.cloud.flight.entity.enums.Currency;
import avia.cloud.flight.repository.FlightRepository;
import avia.cloud.flight.service.IFlightService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class FlightServiceImpl implements IFlightService {
    private final FlightRepository flightRepository;
    private final ModelMapper modelMapper;
    @Override
    public List<FlightDTO> searchFlights(String origin, String destination, boolean oneWay, LocalDate date, Cabin cabin, Currency currency, double minPrice, double maxPrice) {
        return flightRepository.searchFlights(origin,destination,oneWay,date, cabin, currency, minPrice, maxPrice).stream().map(this::convertToFlightDTO).toList();
    }

    private FlightDTO convertToFlightDTO(Flight flight) {
        FlightDTO flightDTO = modelMapper.map(flight, FlightDTO.class);
        flightDTO.setFrom(flight.getOrigin().getCode());
        flightDTO.setTo(flight.getDestination().getCode());
        return flightDTO;
    }
}
