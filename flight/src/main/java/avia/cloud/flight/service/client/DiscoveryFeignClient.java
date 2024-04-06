package avia.cloud.flight.service.client;

import avia.cloud.flight.dto.FaqDTO;
import avia.cloud.flight.dto.SkylineBenefitsDTO;
import avia.cloud.flight.validation.constraint.SupportedLanguage;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("discovery")
public interface DiscoveryFeignClient {
    @GetMapping("/api/faq/global")
    public ResponseEntity<List<FaqDTO>> fetchFaq(@RequestParam @SupportedLanguage String lan, @RequestParam String text);
    @GetMapping("/api/whyUs/global")
    public ResponseEntity<List<SkylineBenefitsDTO>> fetchSkylineBenefits(@RequestParam @SupportedLanguage String lan, @RequestParam String text);
}
