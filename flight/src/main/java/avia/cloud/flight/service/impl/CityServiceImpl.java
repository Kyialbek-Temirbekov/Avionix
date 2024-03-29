package avia.cloud.flight.service.impl;

import avia.cloud.flight.dto.CityDTO;
import avia.cloud.flight.entity.enums.Lan;
import avia.cloud.flight.repository.CityRepository;
import avia.cloud.flight.service.ICityService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CityServiceImpl implements ICityService {
    private final CityRepository cityRepository;

    @Override
    public List<CityDTO> findCities(String lan) {
        return cityRepository.findByLan(Lan.of(lan));
    }
}
