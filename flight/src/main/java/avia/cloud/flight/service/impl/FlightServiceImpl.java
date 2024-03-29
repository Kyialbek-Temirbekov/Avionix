package avia.cloud.flight.service.impl;

import avia.cloud.flight.dto.FlightDTO;
import avia.cloud.flight.dto.SegmentDTO;
import avia.cloud.flight.dto.TariffDTO;
import avia.cloud.flight.entity.Flight;
import avia.cloud.flight.entity.Segment;
import avia.cloud.flight.entity.Tariff;
import avia.cloud.flight.entity.enums.Cabin;
import avia.cloud.flight.entity.enums.Currency;
import avia.cloud.flight.repository.FlightRepository;
import avia.cloud.flight.service.IFlightService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Transactional
@RequiredArgsConstructor
public class FlightServiceImpl implements IFlightService {
    private final FlightRepository flightRepository;
    private final ModelMapper modelMapper;
    @Override
    public HashMap<String, Object> searchFlights(String origin, String destination, boolean oneWay, LocalDate date, Cabin cabin, Currency currency, double minPrice, double maxPrice, Integer stops, Boolean checkedBaggageIncluded, Boolean cabinBaggageIncluded, long minFlightDuration, long maxFlightDuration, long minTransitDuration, long maxTransitDuration, String iata, int page, int pageSize, String direction, String property, String url) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.fromString(direction),property));
        Page<Flight> flightPage = flightRepository.searchFlights(origin,destination,oneWay,date, cabin, currency, minPrice, maxPrice, stops, checkedBaggageIncluded, cabinBaggageIncluded, minFlightDuration, maxFlightDuration, minTransitDuration, maxTransitDuration, iata, pageable);
        List<FlightDTO> flights = flightPage.getContent().stream().map(this::convertToFlightDTO).toList();
        HashMap<String, Object> meta = new HashMap<>();
        long lastPage = flightPage.getTotalElements()/pageSize;
        meta.put("total", flightPage.getTotalElements());
        meta.put("page", page);
        meta.put("pageSize", pageSize);
        meta.put("sortType", property);
        meta.put("next", page != lastPage ? getPageUrl(page + 1, url) : null);
        meta.put("prev", page != 0 ? getPageUrl(page -1, url) : null);
        HashMap<String, Object> response = new HashMap<>();
        response.put("meta", meta);
        response.put("data", flights);
        return response;
    }

    private String getPageUrl(int i, String url) {
        String regex = "(?<=(\\?|&))page=\\d+";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(url);
        return matcher.replaceAll("page=" + i);
    }

    private FlightDTO convertToFlightDTO(Flight flight) {
        FlightDTO flightDTO = modelMapper.map(flight, FlightDTO.class);
        flightDTO.setSegmentDTOS(flight.getSegments().stream().map(this::convertToSegmentDTO).toList());
        flightDTO.setTariffDTO(convertToTariffDTO(flight.getTariff()));
        flightDTO.setFrom(flight.getOrigin().getCode());
        flightDTO.setTo(flight.getDestination().getCode());
        return flightDTO;
    }

    private TariffDTO convertToTariffDTO(Tariff tariff) {
        return modelMapper.map(tariff, TariffDTO.class);
    }

    private SegmentDTO convertToSegmentDTO(Segment segment) {
        return modelMapper.map(segment, SegmentDTO.class);
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
