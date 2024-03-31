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
    @JsonProperty("segments")
    private List<SegmentDTO> segmentDTOS;
    private boolean oneWay;
    private String from;
    private String to;
    private String gate;
    @JsonProperty("tariff")
    private TariffDTO tariffDTO;
    private Currency currency;
    private long flightDuration;
    private long transitDuration;
}
