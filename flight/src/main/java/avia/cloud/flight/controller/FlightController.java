package avia.cloud.flight.controller;

import avia.cloud.flight.dto.FlightDTO;
import avia.cloud.flight.dto.FlightRequestDTO;
import avia.cloud.flight.entity.Flight;
import avia.cloud.flight.entity.enums.Cabin;
import avia.cloud.flight.entity.enums.Currency;
import avia.cloud.flight.entity.enums.FlightStatus;
import avia.cloud.flight.service.IFlightService;
import avia.cloud.flight.validation.constraint.SupportedLanguage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/trip")
@RequiredArgsConstructor
@Validated
public class FlightController {
    private final IFlightService iFlightService;

    @GetMapping("/{flightId}")
    public ResponseEntity<FlightDTO> fetchFlight(@PathVariable String flightId, @RequestParam @SupportedLanguage String lan) {
        return ResponseEntity.status(HttpStatus.OK).body(iFlightService.fetchFlight(flightId,lan));
    }
    @GetMapping("/global")
    public ResponseEntity<Map<String,Object>> globalSearch(@RequestParam String text, @RequestParam @SupportedLanguage String lan) {
        return ResponseEntity.status(HttpStatus.OK).body(iFlightService.globalSearch(text,lan));
    }
    @PostMapping()
    public ResponseEntity<Void> createFlight(@Valid @RequestBody FlightRequestDTO flight, @RequestHeader("Authorization") String token) {
        iFlightService.createFlight(flight,token);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    @GetMapping("/owner")
    public ResponseEntity<HashMap<String,Object>> fetchOwnerFlights(@RequestHeader("Authorization") String token,
                                                             @RequestParam @SupportedLanguage String lan,
                                                             @RequestParam(defaultValue = "0") @PositiveOrZero int page,
                                                             @RequestParam(defaultValue = "8") @PositiveOrZero int pageSize,
                                                             HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(iFlightService.fetchOwnerFlights(token, lan, page, pageSize,request.getHeader("Original-Url")));
    }
    @GetMapping("/seatDetails/{flightId}")
    public ResponseEntity<HashMap<String, Object>> findPlaneSeatDetails(@PathVariable("flightId") String flightId) {
        return ResponseEntity.status(HttpStatus.OK).body(iFlightService.findPlaneSeatDetails(flightId));
    }
    @PatchMapping("/status/{flightId}")
    public ResponseEntity<Void> updateStatus(@PathVariable String flightId, @RequestParam FlightStatus status) {
        iFlightService.updateStatus(flightId, status);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
    @GetMapping()
    public ResponseEntity<HashMap<String, Object>> searchFlights(@RequestParam(required = false) String origin,
                                                                 @RequestParam(required = false) String destination,
                                                                 @RequestParam(required = false) boolean oneWay,
                                                                 @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate departureDate,
                                                                 @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate returnDate,
                                                                 @RequestParam(required = false) Integer adults,
                                                                 @RequestParam(defaultValue = "ECONOMY,PREMIUM_ECONOMY,BUSINESS,FIRST") List<Cabin> cabins,
                                                                 @RequestParam(required = false) Currency currency,
                                                                 @RequestParam(defaultValue = "0.0") @PositiveOrZero double minPrice,
                                                                 @RequestParam(defaultValue = "" + Double.MAX_VALUE) @PositiveOrZero double maxPrice,
                                                                 @RequestParam(required = false) @PositiveOrZero Integer stops,
                                                                 @RequestParam(required = false) Boolean checkedBaggageIncluded,
                                                                 @RequestParam(required = false) Boolean cabinBaggageIncluded,
                                                                 @RequestParam(defaultValue = "0") @PositiveOrZero long minFlightDuration,
                                                                 @RequestParam(defaultValue = "" + Long.MAX_VALUE) @PositiveOrZero long maxFlightDuration,
                                                                 @RequestParam(defaultValue = "0") @PositiveOrZero long minTransitDuration,
                                                                 @RequestParam(defaultValue = "" + Long.MAX_VALUE) @PositiveOrZero long maxTransitDuration,
                                                                 @RequestParam(required = false) String airlineId,
                                                                 @RequestParam(defaultValue = "0") @PositiveOrZero int page,
                                                                 @RequestParam(defaultValue = "20") @PositiveOrZero int pageSize,
                                                                 @RequestParam(defaultValue = "ASC")@Pattern(regexp = "(ASC|DESC)", message = "Invalid input. Allowed values: ASC, DESC") String direction,
                                                                 @RequestParam(defaultValue = "departureFlightDuration") @Pattern(regexp = "(departureFlightDuration|departureTransitDuration|tariff\\.price)", message = "Invalid input. Allowed values: departureFlightDuration, departureTransitDuration, tariff.price") String property,
                                                                 @RequestParam(defaultValue = "en") String lan,
                                                                 HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(iFlightService.searchFlights(origin, destination, oneWay, departureDate, returnDate, adults, cabins, currency, minPrice, maxPrice, stops, checkedBaggageIncluded, cabinBaggageIncluded, minFlightDuration, maxFlightDuration, minTransitDuration, maxTransitDuration, airlineId, page, pageSize, direction, property, lan, request.getHeader("Original-Url")));
    }
}
