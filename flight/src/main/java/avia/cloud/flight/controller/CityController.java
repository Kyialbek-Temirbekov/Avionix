package avia.cloud.flight.controller;

import avia.cloud.flight.dto.CityDTO;
import avia.cloud.flight.service.ICityService;
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
@RequestMapping("/api/city")
@RequiredArgsConstructor
@Validated
public class CityController {
    private final ICityService iCityService;
    @GetMapping()
    public ResponseEntity<List<CityDTO>> findCities(@RequestParam(defaultValue = "en") String lan) {
        return ResponseEntity.status(HttpStatus.OK).body(iCityService.findCities(lan));
    }
}
