package avia.cloud.flight.service.impl;

import avia.cloud.flight.dto.*;
import avia.cloud.flight.entity.Flight;
import avia.cloud.flight.entity.Segment;
import avia.cloud.flight.entity.Tariff;
import avia.cloud.flight.entity.Ticket;
import avia.cloud.flight.entity.enums.Cabin;
import avia.cloud.flight.entity.enums.Currency;
import avia.cloud.flight.entity.enums.FlightStatus;
import avia.cloud.flight.entity.enums.Lan;
import avia.cloud.flight.exception.NotFoundException;
import avia.cloud.flight.repository.AirplaneRepository;
import avia.cloud.flight.repository.CityRepository;
import avia.cloud.flight.repository.FlightRepository;
import avia.cloud.flight.repository.TicketRepository;
import avia.cloud.flight.service.IArticleService;
import avia.cloud.flight.service.IFlightService;
import avia.cloud.flight.service.client.DiscoveryFeignClient;
import avia.cloud.flight.service.client.UserFeignClient;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Transactional
@RequiredArgsConstructor
public class FlightServiceImpl implements IFlightService {
    private final FlightRepository flightRepository;
    private final CityRepository cityRepository;
    private final AirplaneRepository airplaneRepository;
    private final TicketRepository ticketRepository;
    private final UserFeignClient userFeignClient;
    private final DiscoveryFeignClient discoveryFeignClient;
    private final IArticleService articleService;
    private final ModelMapper modelMapper;

    @Override
    public void createFlight(FlightRequestDTO flightRequestDTO, String token) {
        Flight flight = modelMapper.map(flightRequestDTO, Flight.class);
        String airlineId = userFeignClient.findAccountId(token).getBody();
        flight.setAirlineId(airlineId);
        flight.setAirplane(airplaneRepository.findFirstByCabinsCabin(flight.getTariff().getCabin()));
        flight.setOrigin(cityRepository.findById(flightRequestDTO.getOriginCity().getCode()).get());
        flight.setDestination(cityRepository.findById(flightRequestDTO.getDestinationCity().getCode()).get());
        flight.setStatus(FlightStatus.READY);
        flight.setDepartureFlightDuration(calculateFlightDuration(flight.getDepartureSegment()));
        flight.setReturnFlightDuration(calculateFlightDuration(flight.getReturnSegment()));
        flight.setDepartureTransitDuration(calculateTransitDuration(flight.getDepartureSegment()));
        flight.setReturnTransitDuration(calculateTransitDuration(flight.getReturnSegment()));
        flight.getTariff().setFlight(flight);
        flight.getDepartureSegment().forEach(segment -> segment.setDepartureFlight(flight));
        flight.getReturnSegment().forEach(segment -> segment.setReturnFlight(flight));
        flightRepository.save(flight);
    }

    @Override
    public HashMap<String, Object> findPlaneSeatDetails(String flightId) {
        PlaneSeatDetail planeSeatDetail = flightRepository.findPlaneSeatDetails(flightId).orElseThrow(() ->
                new NotFoundException("Flight", "flightId", flightId)
        );
        HashMap<String,Object> seatDetails = new HashMap<>();
        seatDetails.put("airplane", planeSeatDetail);
        seatDetails.put("reservedSeats",ticketRepository.findProjectedByFlightId(flightId).stream().map(Ticket::getSeat).toList());
        return seatDetails;
    }

    @Override
    public HashMap<String, Object> searchFlights(String origin, String destination, boolean oneWay, LocalDate departureDate, LocalDate returnDate, int adults, List<Cabin> cabins, Currency currency, double minPrice, double maxPrice, Integer stops, Boolean checkedBaggageIncluded, Boolean cabinBaggageIncluded, long minFlightDuration, long maxFlightDuration, long minTransitDuration, long maxTransitDuration, String airlineId, int page, int pageSize, String direction, String property, String lan, String url) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.fromString(direction),property));
        Page<Flight> flightPage = flightRepository.searchFlights(origin,destination,oneWay,departureDate, adults, cabins, currency, minPrice, maxPrice, stops, checkedBaggageIncluded, cabinBaggageIncluded, minFlightDuration, maxFlightDuration, minTransitDuration, maxTransitDuration, airlineId, pageable);
        List<FlightDTO> flights = flightPage.getContent().stream().map(flight -> convertToFlightDTO(flight,lan)).toList();
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

    @Override
    public void updateStatus(String flightId, FlightStatus status) {
        Flight flight = flightRepository.findById(flightId).orElseThrow(() ->
                new NotFoundException("Flight", "id", flightId));
        flight.setStatus(status);
        flightRepository.save(flight);
    }

    @Override
    public FlightDTO fetchFlight(String flightId, String lan) {
        return convertToFlightDTO(flightRepository.findById(flightId).orElseThrow(() ->
                new NotFoundException("Flight", "id", flightId)),lan);
    }

    @Override
    public List<FlightDTO> fetchOwnerFlights(String token, String lan) {
        String customerId = userFeignClient.findAccountId(token).getBody();
        return flightRepository.findByAirlineId(customerId).stream().map(flight -> convertToFlightDTO(flight,lan)).toList();
    }

    @Override
    public List<FlightDTO> fetchFlights(String text, String lan) {
        List<String> airlineIds = userFeignClient.findAirlineIds(text).getBody();
        return flightRepository.findAllByText(text,airlineIds, PageRequest.of(0,12)).stream().map(flight -> convertToFlightDTO(flight,lan)).toList();
    }

    @Override
    public Map<String, Object> globalSearch(String text, String lan) {
        Map<String,Object> globalSearch = new HashMap<>();
        globalSearch.put("flights", fetchFlights(text,lan));
        globalSearch.put("topFlights", articleService.findTopFlights(lan,text));
        globalSearch.put("specialDeals", articleService.findSpecialDeals(lan,text));
        ResponseEntity<List<FaqDTO>> faqsResponse = discoveryFeignClient.fetchFaq(lan, text);
        if (faqsResponse.hasBody()) {
            globalSearch.put("faqs", faqsResponse.getBody());
        } else {
            globalSearch.put("faqs", Collections.emptyList());
        }

        ResponseEntity<List<SkylineBenefitsDTO>> whyUsResponse = discoveryFeignClient.fetchSkylineBenefits(lan, text);
        if (whyUsResponse.hasBody()) {
            globalSearch.put("whyUs", whyUsResponse.getBody());
        } else {
            globalSearch.put("whyUs", Collections.emptyMap());
        }

        ResponseEntity<List<CommentDTO>> commentsResponse = userFeignClient.fetchCommentsByText(text);
        if (commentsResponse.hasBody()) {
            globalSearch.put("comments", commentsResponse.getBody());
        } else {
            globalSearch.put("comments", Collections.emptyList());
        }
        return globalSearch;
    }

    private String getPageUrl(int i, String url) {
        String regex = "(?<=(\\?|&))page=\\d+";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(url);
        return matcher.replaceAll("page=" + i);
    }

    public FlightDTO convertToFlightDTO(Flight flight, String lan) {
        FlightDTO flightDTO = modelMapper.map(flight, FlightDTO.class);
        flightDTO.setAirline(userFeignClient.findAirlineName(flight.getAirlineId()).getBody());
        flightDTO.setDepartureTrip(new Itinerary(
                flight.getDepartureFlightDuration(),
                flight.getDepartureTransitDuration(),
                flight.getDepartureSegment().stream().map(segment -> modelMapper.map(segment,SegmentDTO.class)).toList()));
        flightDTO.setReturnTrip(new Itinerary(
                flight.getReturnFlightDuration(),
                flight.getReturnTransitDuration(),
                flight.getReturnSegment().stream().map(segment -> modelMapper.map(segment,SegmentDTO.class)).toList()));
        flightDTO.setTariffDTO(convertToTariffDTO(flight.getTariff()));
        flightDTO.setFrom(cityRepository.findByCodeAndLan(flight.getOrigin().getCode(), Lan.of(lan)));
        flightDTO.setTo(cityRepository.findByCodeAndLan(flight.getDestination().getCode(), Lan.of(lan)));
        return flightDTO;
    }

    private TariffDTO convertToTariffDTO(Tariff tariff) {
        return modelMapper.map(tariff, TariffDTO.class);
    }

    public long calculateFlightDuration(List<Segment> segments) {
        LocalDateTime firstDeparture = segments.get(0).getTakeoffAt();
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
            LocalDateTime nextDeparture = segments.get(i + 1).getTakeoffAt();
            transitDuration += ChronoUnit.MINUTES.between(currentArrival, nextDeparture);
        }
        return transitDuration;
    }
}
