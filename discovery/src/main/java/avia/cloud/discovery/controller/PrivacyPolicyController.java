package avia.cloud.discovery.controller;

import avia.cloud.discovery.dto.UserAgreementDTO;
import avia.cloud.discovery.service.IPrivacyPolicyService;
import avia.cloud.discovery.service.ITermsOfUseService;
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
@RequestMapping("/api/privacyPolicy")
@RequiredArgsConstructor
@Validated
public class PrivacyPolicyController {
    private final IPrivacyPolicyService iPrivacyPolicyService;
    @GetMapping()
    public ResponseEntity<List<UserAgreementDTO>> findPrivacyPolicy(@RequestParam @SupportedLanguage String lan) {
        return ResponseEntity.status(HttpStatus.OK).body(iPrivacyPolicyService.fetchPrivacyPolicy(lan));
    }
}
