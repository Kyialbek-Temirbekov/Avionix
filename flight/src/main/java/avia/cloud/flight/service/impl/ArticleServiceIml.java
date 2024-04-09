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
import avia.cloud.flight.service.client.UserFeignClient;
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
    private final CityRepository cityRepository;
    private final ModelMapper modelMapper;
    private final UserFeignClient userFeignClient;

    @Override
    public List<ArticleDTO> findTopFlights(String lan) {
        return articleRepository.findAll().stream().map(article -> convertToArticleDTO(article, lan)).toList();
    }

    @SneakyThrows
    private ArticleDTO convertToArticleDTO(Article article, String lan) {
        Flight flight = flightRepository.findTopByAirlineIdAndDestinationCode("623e4567-e89b-12d3-a456-556642440005", article.getCityCode()).orElseThrow();
        return new ArticleDTO(ImageUtils.getBase64Image(article.getImage()),
                article.getContent().stream().filter(content -> content.getLan().equals(Lan.of(lan))).findFirst().get().getDescription(),
                null,
                this.convertToFlightDTO(flight,lan));
    }

    @Override
    public List<ArticleDTO> findSpecialDeals(String lan) {
        return specialDealRepository.findAll().stream().map(specialDeal -> convertToArticleDTO(specialDeal, lan)).toList();
    }

    @SneakyThrows
    private ArticleDTO convertToArticleDTO(SpecialDeal specialDeal, String lan) {
        Flight flight = flightRepository.findTopByAirlineIdAndDestinationCode("623e4567-e89b-12d3-a456-556642440005", specialDeal.getCityCode()).orElseThrow();
        return new ArticleDTO(ImageUtils.getBase64Image(specialDeal.getImage()),
                String.format(specialDeal.getContent().stream().filter(content -> content.getLan().equals(Lan.of(lan))).findFirst().get().getDescription(), flight.getTariff().getPrice(), flight.getCurrency().getSign()),
                "10.0",
                this.convertToFlightDTO(flight,lan));
    }

    @Override
    public List<ArticleDTO> findSpecialDeals(String lan, String text) {
        return specialDealRepository.findSpecialDealsByText(text).stream()
                .map(specialDeal -> convertToArticleDTO(specialDeal,lan)).toList();
    }

    @Override
    public List<ArticleDTO> findTopFlights(String lan, String text) {
        return articleRepository.findArticlesByText(text).stream()
                .map(article -> convertToArticleDTO(article,lan)).toList();
    }

    public FlightDTO convertToFlightDTO(Flight flight, String lan) {
        FlightDTO flightDTO = modelMapper.map(flight, FlightDTO.class);
        flightDTO.setAirline(userFeignClient.findAirlineName(flight.getAirlineId()).getBody());
        flightDTO.setDepartureTrip(new Itinerary(
                flight.getDepartureFlightDuration(),
                flight.getDepartureTransitDuration(),
                flight.getDepartureSegment().stream().map(segment -> modelMapper.map(segment,SegmentDTO.class)).toList()));
        flightDTO.setReturnTrip(new Itinerary(
                flight.getReturnFlightDuration(),
                flight.getReturnTransitDuration(),
                flight.getReturnSegment().stream().map(segment -> modelMapper.map(segment,SegmentDTO.class)).toList()));
        flightDTO.setTariffDTO(convertToTariffDTO(flight.getTariff()));
        flightDTO.setFrom(cityRepository.findByCodeAndLan(flight.getOrigin().getCode(), Lan.of(lan)));
        flightDTO.setTo(cityRepository.findByCodeAndLan(flight.getDestination().getCode(), Lan.of(lan)));
        return flightDTO;
    }
    private TariffDTO convertToTariffDTO(Tariff tariff) {
        return modelMapper.map(tariff, TariffDTO.class);
    }
}
