package avia.cloud.flight.dto;

import avia.cloud.flight.entity.City;
import avia.cloud.flight.entity.Segment;
import avia.cloud.flight.entity.Tariff;
import avia.cloud.flight.entity.enums.Currency;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlightRequestDTO {
    @Pattern(regexp = "^[1-9]\\d{0,3}|9999$", message = "Number must contain only digits and must be between 1 and 9999")
    private String number;
    private List<Segment> departureSegment;
    private List<Segment> returnSegment;
    private boolean oneWay;
    private CityRequestDTO originCity;
    private CityRequestDTO destinationCity;
    @NotNull
    @NotBlank
    private String gate;
    private Tariff tariff;
    private Currency currency;
}
