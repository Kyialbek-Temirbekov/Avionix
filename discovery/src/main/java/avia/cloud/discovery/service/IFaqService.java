package avia.cloud.discovery.service;

import avia.cloud.discovery.dto.FaqDTO;

import java.util.List;

public interface IFaqService {
    List<FaqDTO> fetchFaq(String lan);
    List<FaqDTO> fetchFaq(String lan, String text);
}
