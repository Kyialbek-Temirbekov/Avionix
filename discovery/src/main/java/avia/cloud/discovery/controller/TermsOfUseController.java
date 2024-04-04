package avia.cloud.discovery.controller;

import avia.cloud.discovery.dto.UserAgreementDTO;
import avia.cloud.discovery.entity.enums.Role;
import avia.cloud.discovery.service.IPrivacyPolicyService;
import avia.cloud.discovery.service.ITermsOfUseService;
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
@RequestMapping("/api/termsOfUse")
@RequiredArgsConstructor
@Validated
public class TermsOfUseController {
    private final ITermsOfUseService iTermsOfUseService;
    @GetMapping()
    public ResponseEntity<List<UserAgreementDTO>> fetchTermsOfUse(@RequestParam String lan, @RequestParam Role type) {
        return ResponseEntity.status(HttpStatus.OK).body(iTermsOfUseService.fetchTermsOfUse(lan, type));
    }
}
