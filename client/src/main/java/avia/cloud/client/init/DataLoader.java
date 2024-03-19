package avia.cloud.client.init;

import avia.cloud.client.entity.*;
import avia.cloud.client.init.gds.GdsService;
import avia.cloud.client.repository.*;
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
import java.util.List;
import java.util.Objects;

@Component
@Profile("default")
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {
    private final GdsService gdsService;
    private ObjectMapper objectMapper;
    private final AuthorityRepository authorityRepository;
    private final CustomerRepository customerRepository;
    private final CommentRepository commentRepository;
    private final AccountRepository accountRepository;

    @Override
    public void run(String... args) throws IOException {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        loadAuthority("/data/avionix-authority.json");
        loadCustomer("/data/avionix-customer.json");
        loadAirline("/data/avionix-airline-account.json");
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

    private void loadAuthority(String path) throws IOException {
        TypeReference<List<Authority>> typeReference = new TypeReference<>(){};
        InputStream inputStream = TypeReference.class.getResourceAsStream(path);
        List<Authority> authorities = objectMapper.readValue(inputStream, typeReference);
        authorityRepository.saveAllAndFlush(authorities);
    }

    private void loadCustomer(String path) throws IOException {
        TypeReference<List<Customer>> typeReference = new TypeReference<>(){};
        InputStream inputStream = TypeReference.class.getResourceAsStream(path);
        List<Customer> customers = objectMapper.readValue(inputStream, typeReference);
        customerRepository.saveAllAndFlush(customers);
        customers.forEach(customer -> customer.getComments()
                .forEach(comment -> comment.setCustomer(customer)));
        List<Comment> comments = customers.stream().map(Customer::getComments).flatMap(List::stream).toList();
        commentRepository.saveAllAndFlush(comments);

    }

    private void loadAirline(String path) throws IOException {
        TypeReference<List<Account>> typeReference = new TypeReference<>(){};
        InputStream inputStream = TypeReference.class.getResourceAsStream(path);
        List<Account> accounts = objectMapper.readValue(inputStream, typeReference);
        accounts.forEach(account -> {
            Airline airline = gdsService.fetchAirport(account.getAirline().getIata());
            airline.setPriority(account.getAirline().getPriority());
            airline.setAccount(account);
            account.setAirline(airline);
        });
        accountRepository.saveAllAndFlush(accounts);
    }

}

