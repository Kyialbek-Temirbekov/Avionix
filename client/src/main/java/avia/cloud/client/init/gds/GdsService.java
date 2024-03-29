package avia.cloud.client.init.gds;

import avia.cloud.client.entity.Account;
import avia.cloud.client.entity.Airline;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
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

    public static void dev(String[] args) throws IOException {
        GdsService gdsService = new GdsService();
        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference<List<Airline>> typeReference = new TypeReference<>(){};
        InputStream inputStream = TypeReference.class.getResourceAsStream("/data/airline.json");
        List<Airline> airlines = objectMapper.readValue(inputStream, typeReference);
        List<Airline> fetchedAirlines = new ArrayList<>();
        airlines.forEach(airline -> {
            Airline fetchedAirline = gdsService.fetchAirport(airline.getIata());
            fetchedAirline.setPriority(airline.getPriority());
            fetchedAirline.setAccount(airline.getAccount());
            fetchedAirline.getAccount().setAirline(fetchedAirline);
            fetchedAirlines.add(fetchedAirline);
        });
        JsonNode jsonNode = objectMapper.convertValue(fetchedAirlines, JsonNode.class);

        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNode));

        File outputFile = new File("/home/kyialbek/Projects/spring_workspace/APIs/Avionix/client/src/main/resources/data/avionix-airline.json");
        try {
            objectMapper.writeValue(outputFile, jsonNode);
        } catch (IOException e) {
            System.err.println("Error writing JSON to file: " + e.getMessage());
        }
    }

}
