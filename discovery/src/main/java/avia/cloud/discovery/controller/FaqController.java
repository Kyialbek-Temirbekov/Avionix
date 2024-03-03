package avia.cloud.discovery.controller;

import avia.cloud.discovery.dto.FaqDTO;
import avia.cloud.discovery.dto.SkylineBenefitsDTO;
import avia.cloud.discovery.service.IFaqService;
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
@RequestMapping("/api/faq")
@RequiredArgsConstructor
@Validated
public class FaqController {
    private final IFaqService iFaqService;
    @GetMapping()
    public ResponseEntity<List<FaqDTO>> fetchFaq(@RequestParam @SupportedLanguage String lan) {
        return ResponseEntity.status(HttpStatus.OK).body(iFaqService.fetchFaq(lan));
    }
}
