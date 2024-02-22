package avia.cloud.discovery.init;

import avia.cloud.discovery.repository.FaqRepository;
import avia.cloud.discovery.repository.SkylineBenefitsRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.ws.rs.NotFoundException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.data.jpa.repository.JpaRepository;

import java.lang.reflect.Field;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

@Component
@Profile("default")
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {
    private final SkylineBenefitsRepository skylineBenefitsRepository;
    private final FaqRepository faqRepository;

    @Override
    public void run(String... args) throws IOException {
        loadData(new TypeReference<>() {}, "/data/avionix-skyline-benefits.json", skylineBenefitsRepository, "content");
        loadData(new TypeReference<>() {}, "/data/avionix-faq.json", faqRepository, "content");

        loadFiles("classpath*:data/files/skyline-benefits/*",skylineBenefitsRepository, "logo");
    }

    private <T> void loadFiles(String pattern, JpaRepository<T, String> jpaRepository, String requiredField) throws IOException {
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

    private <T> void loadData(TypeReference<List<T>> typeReference, String path, JpaRepository<T, String> repository, String listField) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        InputStream inputStream = TypeReference.class.getResourceAsStream(path);
        try {
            List<T> entities = objectMapper.readValue(inputStream, typeReference);
            setOwner(entities,listField);
            repository.saveAll(entities);
        } catch (IOException e) {
            throw new RuntimeException("Unable to load data from json file: " + e.getMessage());
        }
    }
    @SuppressWarnings("unchecked")
    private <T> void setOwner(List<T> entities, String listFieldName) {
        for(T entity : entities) {
            try {
                Field listField = entity.getClass().getDeclaredField(listFieldName);
                listField.setAccessible(true);
                for(T item : (List<T>)listField.get(entity)) {
                    Field[] fields = item.getClass().getDeclaredFields();
                    Field ownerField = null;
                    for(Field field : fields) {
                        if(field.getType().equals(entity.getClass())) {
                            ownerField = field;
                        }
                    }
                    Objects.requireNonNull(ownerField).setAccessible(true);
                    ownerField.set(item,entity);
                }
            } catch (Exception e) {
                throw new RuntimeException("Error setting owner " + e);
            }
        }
    }

}

