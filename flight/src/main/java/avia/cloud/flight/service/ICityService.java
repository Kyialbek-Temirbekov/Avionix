package avia.cloud.flight.service;

import avia.cloud.flight.dto.CityDTO;

import java.util.List;

public interface ICityService {
    List<CityDTO> findCities(String lan);
}
