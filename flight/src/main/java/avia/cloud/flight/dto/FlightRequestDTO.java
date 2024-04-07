package avia.cloud.flight.dto;

import avia.cloud.flight.entity.City;
import avia.cloud.flight.entity.Segment;
import avia.cloud.flight.entity.Tariff;
import avia.cloud.flight.entity.enums.Currency;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlightRequestDTO {
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
