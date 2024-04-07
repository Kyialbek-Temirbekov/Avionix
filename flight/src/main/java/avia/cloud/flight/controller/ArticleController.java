package avia.cloud.flight.controller;

import avia.cloud.flight.dto.ArticleDTO;
import avia.cloud.flight.service.IArticleService;
import avia.cloud.flight.validation.constraint.SupportedLanguage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/article")
@RequiredArgsConstructor
@Validated
public class ArticleController {
    private final IArticleService iArticleService;
    @GetMapping("/topFlight")
    ResponseEntity<List<ArticleDTO>> findTopFlights(@RequestParam @SupportedLanguage String lan) {
        return ResponseEntity.ok().body(iArticleService.findTopFlights(lan));
    }
    @GetMapping("/specialDeal")
    ResponseEntity<List<ArticleDTO>> findTopSpecialDeals(@RequestParam @SupportedLanguage String lan) {
        return ResponseEntity.ok().body(iArticleService.findSpecialDeals(lan));
    }
}
