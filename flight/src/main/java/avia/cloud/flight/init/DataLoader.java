package avia.cloud.flight.init;

import avia.cloud.flight.entity.City;
import avia.cloud.flight.entity.Flight;
import avia.cloud.flight.entity.enums.Cabin;
import avia.cloud.flight.entity.enums.FlightStatus;
import avia.cloud.flight.init.gds.GdsService;
import avia.cloud.flight.repository.CityRepository;
import avia.cloud.flight.repository.FlightRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
@Profile("default")
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {
    private final GdsService gdsService;
    private final CityRepository cityRepository;
    private final FlightRepository flightRepository;
    private ObjectMapper objectMapper;


    @Override
    public void run(String... args) throws IOException {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        loadCity("/data/avionix-city.json");
        loadFlight("/data/avionix-flight-offers.json");
    }

    private <T> void loadFile(String pattern, JpaRepository<T, String> jpaRepository, String requiredField) throws IOException {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] foundResources = resolver.getResources(pattern);
        for(Resource resource: foundResources) {
            try {
            String id = resource.getFilename();
            T entity = jpaRepository.findById(Objects.requireNonNull(Objects.requireNonNull(id).substring(0,id.lastIndexOf('.')))).orElseThrow(NotFoundException::new);
            Field field = entity.getClass().getDeclaredField(requiredField);
            field.setAccessible(true);
            field.set(entity, StreamUtils.copyToByteArray(resource.getInputStream()));
            jpaRepository.save(entity);
            } catch (Exception e) {
                throw new RuntimeException("Error setting file " + e);
            }
        }
    }

    private void loadCity(String path) throws IOException {
        TypeReference<List<City>> typeReference = new TypeReference<>(){};
        InputStream inputStream = TypeReference.class.getResourceAsStream(path);
        List<City> cities = objectMapper.readValue(inputStream, typeReference);
        cities.forEach(city -> city.getNames()
                .forEach(cityNames -> cityNames.setCity(city)));
        cityRepository.saveAllAndFlush(cities);
    }

    private void loadFlight(String path) throws IOException {
        TypeReference<List<Map<String, String>>> typeReference = new TypeReference<>() {};
        InputStream inputStream = TypeReference.class.getResourceAsStream(path);
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
                    6,
                    Cabin.BUSINESS.toString()
            );
            fetchedFlights.forEach(flight -> {
                flight.getTariffs().forEach(tariff -> tariff.setFlight(flight));
                flight.getSegments().forEach(segment -> segment.setFlight(flight));
                flight.setAirlineId(flightOffer.get("airlineId"));
                flight.setOrigin(cityRepository.findById(originCode).get());
                flight.setDestination(cityRepository.findById(destinationCode).get());
                flight.setStatus(FlightStatus.READY);
            });
            flights.addAll(fetchedFlights);
        });

        flightRepository.saveAllAndFlush(flights);
    }

}

