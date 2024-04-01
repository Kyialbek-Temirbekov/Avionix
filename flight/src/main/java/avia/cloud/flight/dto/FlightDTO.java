package avia.cloud.flight.dto;

import avia.cloud.flight.entity.enums.Currency;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlightDTO {
    private String id;
    @JsonProperty("departureSegment")
    private List<SegmentDTO> departureSegmentDTOS;
    @JsonProperty("returnSegment")
    private List<SegmentDTO> returnSegmentDTOS;
    private boolean oneWay;
    private String from;
    private String to;
    private String gate;
    @JsonProperty("tariff")
    private TariffDTO tariffDTO;
    private Currency currency;
    private long departureFlightDuration;
    private long departureTransitDuration;
    private long returnFlightDuration;
    private long returnTransitDuration;
}
