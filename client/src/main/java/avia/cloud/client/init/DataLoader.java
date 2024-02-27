package avia.cloud.client.init;

import avia.cloud.client.entity.Authority;
import avia.cloud.client.repository.AuthorityRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private final ObjectMapper objectMapper;
    private final AuthorityRepository authorityRepository;

    @Override
    public void run(String... args) throws IOException {
        loadAuthority("/data/avionix-authority.json");
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
        authorityRepository.saveAll(authorities);
    }

}

