package avia.cloud.discovery.init;

import avia.cloud.discovery.entity.Faq;
import avia.cloud.discovery.entity.PrivacyPolicy;
import avia.cloud.discovery.entity.SkylineBenefits;
import avia.cloud.discovery.entity.TermsOfUse;
import avia.cloud.discovery.repository.FaqRepository;
import avia.cloud.discovery.repository.PrivacyPolicyRepository;
import avia.cloud.discovery.repository.SkylineBenefitsRepository;
import avia.cloud.discovery.repository.TermsOfUseRepository;
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
    private ObjectMapper objectMapper;
    private final SkylineBenefitsRepository skylineBenefitsRepository;
    private final FaqRepository faqRepository;
    private final PrivacyPolicyRepository privacyPolicyRepository;
    private final TermsOfUseRepository termsOfUseRepository;

    @Override
    public void run(String... args) throws IOException {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        loadSkylineBenefits("/data/avionix-skyline-benefits.json");
        loadFaq("/data/avionix-faq.json");
        loadPrivacyPolicy("/data/avionix-privacy-policy.json");
        loadTermsOfUse("/data/avionix-terms-of-use.json");

        loadFile("classpath*:data/files/skyline-benefits/*",skylineBenefitsRepository, "logo");
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

    private void loadSkylineBenefits(String path) throws IOException {
        TypeReference<List<SkylineBenefits>> typeReference = new TypeReference<>(){};
        InputStream inputStream = TypeReference.class.getResourceAsStream(path);
        List<SkylineBenefits> skylineBenefits = objectMapper.readValue(inputStream, typeReference);
        skylineBenefits.forEach(skylineBenefit -> skylineBenefit.getContent()
                .forEach(skylineBenefitsContent -> skylineBenefitsContent.setSkylineBenefits(skylineBenefit)));
        skylineBenefitsRepository.saveAll(skylineBenefits);
    }
    private void loadFaq(String path) throws IOException {
        TypeReference<List<Faq>> typeReference = new TypeReference<>(){};
        InputStream inputStream = TypeReference.class.getResourceAsStream(path);
        List<Faq> faqs = objectMapper.readValue(inputStream, typeReference);
        faqs.forEach(faq -> faq.getContent()
                .forEach(faqContent -> faqContent.setFaq(faq)));
        faqRepository.saveAll(faqs);
    }
    private void loadPrivacyPolicy(String path) throws IOException {
        TypeReference<List<PrivacyPolicy>> typeReference = new TypeReference<>(){};
        InputStream inputStream = TypeReference.class.getResourceAsStream(path);
        List<PrivacyPolicy> privacyPolicies = objectMapper.readValue(inputStream, typeReference);
        privacyPolicies.forEach(privacyPolicy -> privacyPolicy.getContent()
                .forEach(privacyPolicyContent -> privacyPolicyContent.setPrivacyPolicy(privacyPolicy)));
        privacyPolicyRepository.saveAll(privacyPolicies);
    }
    private void loadTermsOfUse(String path) throws IOException {
        TypeReference<List<TermsOfUse>> typeReference = new TypeReference<>(){};
        InputStream inputStream = TypeReference.class.getResourceAsStream(path);
        List<TermsOfUse> termsOfUses = objectMapper.readValue(inputStream, typeReference);
        termsOfUses.forEach(termsOfUse -> termsOfUse.getContent()
                .forEach(termsOfUseContent -> termsOfUseContent.setTermsOfUse(termsOfUse)));
        termsOfUseRepository.saveAll(termsOfUses);
    }
}

