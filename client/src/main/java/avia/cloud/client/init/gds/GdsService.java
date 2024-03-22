package avia.cloud.client.init.gds;

import avia.cloud.client.entity.Account;
import avia.cloud.client.entity.Airline;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

@Slf4j
public class GdsService extends RestTemplate {
    private static final String airportSearchApi = "https://test.api.amadeus.com/v1/reference-data/locations";

    private final GdsTokenProvider tokenProvider = new GdsTokenProvider();

    public Airline fetchAirport(String iata) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(airportSearchApi)
                .queryParam("subType", "AIRPORT")
                .queryParam("keyword", iata);
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

    public static void main(String[] args) throws IOException {
        GdsService gdsService = new GdsService();
        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference<List<Account>> typeReference = new TypeReference<>(){};
        InputStream inputStream = TypeReference.class.getResourceAsStream("/data/avionix-airline.json");
        List<Account> accounts = objectMapper.readValue(inputStream, typeReference);
        accounts.forEach(account -> {
            Airline airline = gdsService.fetchAirport(account.getAirline().getIata());
            airline.setPriority(account.getAirline().getPriority());
            airline.setAccount(account);
            account.setAirline(airline);
        });
        JsonNode jsonNode = objectMapper.convertValue(accounts, JsonNode.class);
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNode));
    }

}
