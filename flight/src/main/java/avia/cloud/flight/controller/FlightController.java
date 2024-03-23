package avia.cloud.flight.controller;

import avia.cloud.flight.dto.FlightDTO;
import avia.cloud.flight.entity.Flight;
import avia.cloud.flight.entity.enums.Cabin;
import avia.cloud.flight.entity.enums.Currency;
import avia.cloud.flight.repository.FlightRepository;
import avia.cloud.flight.service.IFlightService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/trip")
@RequiredArgsConstructor
@Validated
public class FlightController {
    private final IFlightService iFlightService;
    @GetMapping()
    public ResponseEntity<List<FlightDTO>> searchFlights(@RequestParam String origin,
                                                         @RequestParam String destination,
                                                         @RequestParam boolean oneWay,
                                                         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                                         @RequestParam int adults, @RequestParam(required = false) Cabin cabin,
                                                         @RequestParam(required = false)Currency currency,
                                                         @RequestParam(defaultValue = "0.0") double minPrice,
                                                         @RequestParam(defaultValue = "" + Double.MAX_VALUE) double maxPrice,
                                                         @RequestParam(required = false) Integer stops,
                                                         @RequestParam(required = false) Boolean checkedBaggageIncluded,
                                                         @RequestParam(required = false) Boolean cabinBaggageIncluded,
                                                         @RequestParam(defaultValue = "0") long minFlightDuration,
                                                         @RequestParam(defaultValue = "" + Long.MAX_VALUE) long maxFlightDuration,
                                                         @RequestParam(defaultValue = "0") long minTransitDuration,
                                                         @RequestParam(defaultValue = "" + Long.MAX_VALUE) long maxTransitDuration) {
        return ResponseEntity.status(HttpStatus.OK).body(iFlightService.searchFlights(origin, destination, oneWay, date, cabin, currency, minPrice, maxPrice, stops, checkedBaggageIncluded, cabinBaggageIncluded, minFlightDuration, maxFlightDuration, minTransitDuration, maxTransitDuration));
    }
}
