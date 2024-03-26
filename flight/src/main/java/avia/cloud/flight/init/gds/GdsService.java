package avia.cloud.flight.init.gds;

import avia.cloud.flight.entity.City;
import avia.cloud.flight.entity.Flight;
import avia.cloud.flight.entity.Segment;
import avia.cloud.flight.entity.Tariff;
import avia.cloud.flight.entity.enums.Cabin;
import avia.cloud.flight.entity.enums.Currency;
import avia.cloud.flight.entity.enums.FlightStatus;
import avia.cloud.flight.service.impl.FlightServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class GdsService extends RestTemplate {
    private static final String flightOffersSearchApi = "https://test.api.amadeus.com/v2/shopping/flight-offers";

    private final GdsTokenProvider tokenProvider = new GdsTokenProvider();
    private final FlightServiceImpl flightService = new FlightServiceImpl(null,null);

    public List<Flight> fetchFlights(String originLocationCode, String destinationLocationCode, String departureDate, int adults, int limit, String travelClass) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(flightOffersSearchApi)
                .queryParam("originLocationCode", originLocationCode)
                .queryParam("destinationLocationCode", destinationLocationCode)
                .queryParam("departureDate", departureDate)
                .queryParam("adults", adults)
                .queryParam("max", limit)
                .queryParam("travelClass", travelClass);
        String url = builder.toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("Authorization", "Bearer " + tokenProvider.fetchToken());

        HttpEntity<?> request = new HttpEntity<>(headers);

        ResponseEntity<String> responseEntity = exchange(
                url,
                HttpMethod.GET,
                request,
                String.class
        );

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = null;

        try {
            jsonNode = objectMapper.readTree(responseEntity.getBody());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        List<JsonNode> nodeList = new ArrayList<>();
        for(JsonNode node: jsonNode.get("data")) {
            nodeList.add(node);
        }
        return nodeList.stream().map(this::convertToFlight).toList();
    }

    private Flight convertToFlight(JsonNode jsonNode) {
        JsonNode segmentNode = jsonNode.get("itineraries").get(0).get("segments");
        JsonNode tariffNode = jsonNode.get("travelerPricings").get(0);

        List<Segment> segments = new ArrayList<>();
        for(JsonNode segment: segmentNode) {
            segments.add(Segment.builder()
                    .departureIata(segment.get("departure").get("iataCode").asText())
                    .departureAt(LocalDateTime.parse(segment.get("departure").get("at").asText()))
                    .arrivalIata(segment.get("arrival").get("iataCode").asText())
                    .arrivalAt(LocalDateTime.parse(segment.get("arrival").get("at").asText())).build());
        }
        Tariff tariff = Tariff.builder()
                .price(jsonNode.get("price").get("base").asDouble())
                .cabin(Cabin.valueOf(tariffNode.get("fareDetailsBySegment").get(0).get("cabin").asText()))
                .checkedBaggageIncluded(true)
                .cabinBaggageIncluded(true).build();

        return Flight.builder()
                .segments(segments)
                .oneWay(jsonNode.get("oneWay").asBoolean())
                .gate("A" + jsonNode.get("numberOfBookableSeats").asText())
                .tariff(tariff)
                .currency(Currency.valueOf(jsonNode.get("price").get("currency").asText()))
                .flightDuration(flightService.calculateFlightDuration(segments))
                .transitDuration(flightService.calculateTransitDuration(segments)).build();
    }

    public static void dev(String[] args) throws IOException {
        GdsService gdsService = new GdsService();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        TypeReference<List<Map<String, String>>> typeReference = new TypeReference<>() {};
        InputStream inputStream = TypeReference.class.getResourceAsStream("/data/flight-offers.json");
        List<Map<String, String>> flightOffers = objectMapper.readValue(inputStream, typeReference);
        List<Flight> flights = new ArrayList<>();

        flightOffers.forEach(flightOffer -> {
            String originCode = flightOffer.get("originCode");
            String destinationCode = flightOffer.get("destinationCode");
            List<Flight> fetchedFlights = gdsService.fetchFlights(
                    originCode,
                    destinationCode,
                    LocalDate.of(2024,3,26).toString(),
                    1,
                    40,
                    Cabin.BUSINESS.toString()
            );
            fetchedFlights.forEach(flight -> {
                flight.getTariff().setFlight(flight);
                flight.getSegments().forEach(segment -> segment.setFlight(flight));
                flight.setIata(flightOffer.get("iata"));
                City origin = new City();
                origin.setCode(originCode);
                flight.setOrigin(origin);
                City destination = new City();
                destination.setCode(destinationCode);
                flight.setDestination(destination);
                flight.setStatus(FlightStatus.READY);
            });
            flights.addAll(fetchedFlights);
        });

        JsonNode jsonNode = objectMapper.convertValue(flights, JsonNode.class);
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNode));

        File outputFile = new File("/home/kyialbek/Projects/spring_workspace/APIs/Avionix/flight/src/main/resources/data/avionix-flight.json");
        try {
            objectMapper.writeValue(outputFile, jsonNode);
        } catch (IOException e) {
            System.err.println("Error writing JSON to file: " + e.getMessage());
        }
    }

}
