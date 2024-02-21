package avia.cloud.discovery.init;

import avia.cloud.discovery.entity.SkylineBenefits;
import avia.cloud.discovery.entity.SkylineBenefitsContent;
import avia.cloud.discovery.repository.SkylineBenefitsRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Component
@Profile("default")
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {
    private final SkylineBenefitsRepository skylineBenefitsRepository;;
    @Override
    public void run(String... args) {
        loadData(new TypeReference<>() {}, "/data/avionix-skyline-benefits.json", skylineBenefitsRepository);
    }
    protected <T> void loadData(TypeReference<List<T>> typeReference, String path, JpaRepository<T, String> repository) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        InputStream inputStream = TypeReference.class.getResourceAsStream(path);
        try {
            List<T> entities = objectMapper.readValue(inputStream, typeReference);
            for (T entity : entities) {
                if (entity instanceof SkylineBenefits) {
                    establishSkylineBenefitsRelationships((SkylineBenefits) entity);
                }
            }
            repository.saveAll(entities);
        } catch (IOException e) {
            throw new RuntimeException("Unable to load data from json file: " + e.getMessage());
        }
    }
    private void establishSkylineBenefitsRelationships(SkylineBenefits skylineBenefits) {
        for (SkylineBenefitsContent content : skylineBenefits.getContent()) {
            content.setSkylineBenefits(skylineBenefits);
        }
    }

}

