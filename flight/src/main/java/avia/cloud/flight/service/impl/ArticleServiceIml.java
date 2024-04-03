package avia.cloud.flight.service.impl;

import avia.cloud.flight.dto.*;
import avia.cloud.flight.entity.Article;
import avia.cloud.flight.entity.Flight;
import avia.cloud.flight.entity.SpecialDeal;
import avia.cloud.flight.entity.Tariff;
import avia.cloud.flight.entity.enums.Lan;
import avia.cloud.flight.repository.ArticleRepository;
import avia.cloud.flight.repository.CityRepository;
import avia.cloud.flight.repository.FlightRepository;
import avia.cloud.flight.repository.SpecialDealRepository;
import avia.cloud.flight.service.IArticleService;
import avia.cloud.flight.service.IFlightService;
import avia.cloud.flight.util.ImageUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ArticleServiceIml implements IArticleService {
    private final ArticleRepository articleRepository;
    private final SpecialDealRepository specialDealRepository;
    private final FlightRepository flightRepository;
    private final IFlightService flightService;

    @Override
    public List<ArticleDTO> findTopFlights(String lan) {
        return articleRepository.findAll().stream().map(article -> convertToArticleDTO(article, lan)).toList();
    }

    @SneakyThrows
    private ArticleDTO convertToArticleDTO(Article article, String lan) {
        Flight flight = flightRepository.findTopByIataAndDestinationCode("LAX", article.getCityCode()).orElseThrow();
        System.out.println(flight.getId());
        return new ArticleDTO(ImageUtils.getBase64Image(article.getImage()),
                article.getContent().stream().filter(content -> content.getLan().equals(Lan.of(lan))).findFirst().get().getDescription(),
                null,
                flightService.convertToFlightDTO(flight,lan));
    }

    @Override
    public List<ArticleDTO> findSpecialDeals(String lan) {
        return specialDealRepository.findAll().stream().map(specialDeal -> convertToArticleDTO(specialDeal, lan)).toList();
    }

    @SneakyThrows
    private ArticleDTO convertToArticleDTO(SpecialDeal specialDeal, String lan) {
        Flight flight = flightRepository.findTopByIataAndDestinationCode("LAX", specialDeal.getCityCode()).orElseThrow();
        return new ArticleDTO(ImageUtils.getBase64Image(specialDeal.getImage()),
                String.format(specialDeal.getContent().stream().filter(content -> content.getLan().equals(Lan.of(lan))).findFirst().get().getDescription(), flight.getTariff().getPrice(), flight.getCurrency()),
                "10.0",
                flightService.convertToFlightDTO(flight,lan));
    }
}
