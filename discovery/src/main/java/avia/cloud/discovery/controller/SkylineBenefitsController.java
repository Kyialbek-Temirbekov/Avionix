package avia.cloud.discovery.controller;

import avia.cloud.discovery.dto.SkylineBenefitsDTO;
import avia.cloud.discovery.service.ISkylineBenefitsService;
import avia.cloud.discovery.validation.constraint.SupportedLanguage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/whyUs")
@RequiredArgsConstructor
@Validated
public class SkylineBenefitsController {
    private final ISkylineBenefitsService iSkylineBenefitsService;
    @GetMapping()
    public ResponseEntity<List<SkylineBenefitsDTO>> fetchSkylineBenefits(@RequestParam @SupportedLanguage String lan) {
        return ResponseEntity.status(HttpStatus.OK).body(iSkylineBenefitsService.fetchSkylineBenefits(lan));
    }
    @GetMapping("/global")
    public ResponseEntity<List<SkylineBenefitsDTO>> fetchSkylineBenefits(@RequestParam @SupportedLanguage String lan, @RequestParam String text) {
        return ResponseEntity.status(HttpStatus.OK).body(iSkylineBenefitsService.fetchSkylineBenefits(lan,text));
    }
}
