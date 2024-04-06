package avia.cloud.flight.service;

import avia.cloud.flight.dto.ArticleDTO;

import java.util.List;

public interface IArticleService {
    List<ArticleDTO> findSpecialDeals(String lan);
    List<ArticleDTO> findTopFlights(String lan);
    List<ArticleDTO> findSpecialDeals(String lan, String text);
    List<ArticleDTO> findTopFlights(String lan, String text);
}
