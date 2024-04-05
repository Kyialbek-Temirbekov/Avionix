package avia.cloud.flight.init;

import avia.cloud.flight.entity.*;
import avia.cloud.flight.entity.enums.TicketStatus;
import avia.cloud.flight.repository.*;
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
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Component
@Profile("default")
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {
    private final CityRepository cityRepository;
    private final FlightRepository flightRepository;
    private final AirplaneRepository airplaneRepository;
    private final TicketRepository ticketRepository;
    private final ArticleRepository articleRepository;
    private final SpecialDealRepository specialDealRepository;
    private ObjectMapper objectMapper;


    @Override
    public void run(String... args) throws IOException {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        loadCity("/data/avionix-city.json");
        loadAirplane("/data/avionix-airplane.json");
        loadFlight("/data/avionix-flight.json");
        loadArticle("/data/avionix-article.json");
        loadSpecialDeal("/data/avionix-special-deal.json");

        loadFile("/data/files/top-flights/*", articleRepository, "image");
        loadFile("/data/files/special-deals/*", specialDealRepository, "image");
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

    private void loadAirplane(String path) throws IOException {
        TypeReference<List<Airplane>> typeReference = new TypeReference<>(){};
        InputStream inputStream = TypeReference.class.getResourceAsStream(path);
        List<Airplane> airplanes = objectMapper.readValue(inputStream, typeReference);
        airplanes.forEach(airplane -> airplane.getCabins().forEach(cabin -> cabin.setAirplane(airplane)));
        airplaneRepository.saveAllAndFlush(airplanes);
    }

    private void loadFlight(String path) throws IOException {
        TypeReference<List<Flight>> typeReference = new TypeReference<>() {};
        InputStream inputStream = TypeReference.class.getResourceAsStream(path);
        List<Flight> flights = objectMapper.readValue(inputStream, typeReference);
        flights.forEach(flight -> {
            flight.getDepartureSegment().forEach(segment -> segment.setDepartureFlight(flight));
            flight.getReturnSegment().forEach(segment -> segment.setReturnFlight(flight));
            flight.getTariff().setFlight(flight);
            flight.setOrigin(cityRepository.findById(flight.getOrigin().getCode()).orElseThrow());
            flight.setDestination(cityRepository.findById(flight.getDestination().getCode()).orElseThrow());
            flight.setAirplane(airplaneRepository.findFirstByCabinsCabin(flight.getTariff().getCabin()));
            flight.setTickets(createSimpleTickets());
        });

        flightRepository.saveAllAndFlush(flights);
        flights.forEach(flight -> {
            flight.getTickets().forEach(ticket -> ticket.setFlight(flight));
            ticketRepository.saveAllAndFlush(flight.getTickets());
        });
    }

    private void loadArticle(String path) throws IOException {
        TypeReference<List<Article>> typeReference = new TypeReference<>(){};
        InputStream inputStream = TypeReference.class.getResourceAsStream(path);
        List<Article> articles = objectMapper.readValue(inputStream, typeReference);
        articles.forEach(article -> {
            article.getContent().forEach(content -> content.setArticle(article));
        });
        articleRepository.saveAllAndFlush(articles);
    }

    private void loadSpecialDeal(String path) throws IOException {
        TypeReference<List<SpecialDeal>> typeReference = new TypeReference<>(){};
        InputStream inputStream = TypeReference.class.getResourceAsStream(path);
        List<SpecialDeal> specialDeals = objectMapper.readValue(inputStream, typeReference);
        specialDeals.forEach(specialDeal -> {
            specialDeal.getContent().forEach(content -> content.setSpecialDeal(specialDeal));
        });
        specialDealRepository.saveAllAndFlush(specialDeals);
    }

    private List<Ticket> createSimpleTickets() {
        Ticket ticket1 = Ticket.builder()
                .customerId("38400000-8cf0-11bd-b23e-10b96e4ef00d")
                .seat("A1")
                .checkedBaggageIncluded(true)
                .price(100.0)
                .status(TicketStatus.RESERVED)
                .build();

        Ticket ticket2 = Ticket.builder()
                .customerId("38400000-8cf0-11bd-b23e-10b96e4ef00d")
                .seat("A3")
                .checkedBaggageIncluded(false)
                .price(200.0)
                .status(TicketStatus.RESERVED)
                .build();

        Ticket ticket3 = Ticket.builder()
                .customerId("38400000-8cf0-11bd-b23e-10b96e4ef00d")
                .seat("B3")
                .checkedBaggageIncluded(true)
                .price(150.0)
                .status(TicketStatus.RESERVED)
                .build();

        Ticket ticket4 = Ticket.builder()
                .customerId("38400000-8cf0-11bd-b23e-10b96e4ef00d")
                .seat("A5")
                .checkedBaggageIncluded(false)
                .price(120.0)
                .status(TicketStatus.RESERVED)
                .build();

        Ticket ticket5 = Ticket.builder()
                .customerId("38400000-8cf0-11bd-b23e-10b96e4ef00d")
                .seat("A7")
                .checkedBaggageIncluded(true)
                .price(250.0)
                .status(TicketStatus.RESERVED)
                .build();

        Ticket ticket6 = Ticket.builder()
                .customerId("38400000-8cf0-11bd-b23e-10b96e4ef00d")
                .seat("D8")
                .checkedBaggageIncluded(false)
                .price(110.0)
                .status(TicketStatus.RESERVED)
                .build();

        return Arrays.asList(ticket1, ticket2, ticket3, ticket4, ticket5, ticket6);
    }

}

