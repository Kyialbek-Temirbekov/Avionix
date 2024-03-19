package avia.cloud.flight.init.gds;

import avia.cloud.flight.entity.Flight;
import avia.cloud.flight.entity.Segment;
import avia.cloud.flight.entity.Tariff;
import avia.cloud.flight.entity.enums.Cabin;
import avia.cloud.flight.entity.enums.Currency;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class GdsService extends RestTemplate {
    @Value("${application.amadeus.client.key}")
    public String clientId;
    @Value("${application.amadeus.client.secret}")
    public String clientSecret;

    @Value("${application.amadeus.apis.flightOffersSearch}")
    public String flightOffersSearch;

    private final GdsTokenProvider tokenProvider;

    public List<Flight> fetchFlights(String originLocationCode, String destinationLocationCode, String departureDate, int adults, int limit, String travelClass) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(flightOffersSearch)
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
        headers.set("Authorization", "Bearer " + tokenProvider.getAccessToken());

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
        List<Tariff> tariffs = new ArrayList<>();
        for(JsonNode segment: segmentNode) {
            segments.add(Segment.builder()
                    .departureIata(segment.get("departure").get("iataCode").asText())
                    .departureAt(LocalDateTime.parse(segment.get("departure").get("at").asText()))
                    .arrivalIata(segment.get("arrival").get("iataCode").asText())
                    .arrivalAt(LocalDateTime.parse(segment.get("arrival").get("at").asText())).build());
        }
        tariffs.add(Tariff.builder()
                .price(jsonNode.get("price").get("base").asDouble())
                .cabin(Cabin.valueOf(tariffNode.get("fareDetailsBySegment").get(0).get("cabin").asText())).build());

        return Flight.builder()
                .segments(segments)
                .oneWay(jsonNode.get("oneWay").asBoolean())
                .gate("A" + jsonNode.get("numberOfBookableSeats").asText())
                .tariffs(tariffs)
                .currency(Currency.valueOf(jsonNode.get("price").get("currency").asText())).build();
    }

}
