package avia.cloud.client.init.gds;

import avia.cloud.client.entity.Airline;
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

import java.util.Collections;

@Component
@RequiredArgsConstructor
@Slf4j
public class GdsService extends RestTemplate {
    @Value("${application.amadeus.client.key}")
    public String clientId;
    @Value("${application.amadeus.client.secret}")
    public String clientSecret;

    @Value("${application.amadeus.apis.airportSearch}")
    public String airportSearchApi;

    private final GdsTokenProvider tokenProvider;

    public Airline fetchAirport(String iata) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(airportSearchApi)
                .queryParam("subType", "AIRPORT")
                .queryParam("keyword", iata);
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
        int count = jsonNode.get("meta").get("count").asInt();
        if(count < 1) {
            log.error("Airport count is " + count + " for iata code: " + iata);
        }
        return convertToAirline(jsonNode.get("data").get(0));
    }

    private Airline convertToAirline(JsonNode jsonNode) {
        return Airline.builder()
                .iata(jsonNode.get("iataCode").asText())
                .name(jsonNode.get("name").asText())
                .cityCode(jsonNode.get("address").get("cityCode").asText())
                .officialWebsiteUrl(jsonNode.get("self").get("href").asText())
                .description(jsonNode.get("detailedName").asText()).build();
    }

}
