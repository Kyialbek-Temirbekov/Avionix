package avia.cloud.flight.dto;

import avia.cloud.flight.entity.enums.Currency;
import avia.cloud.flight.entity.enums.FlightStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlightDTO {
    private String id;
    private String airline;
    private boolean oneWay;
    private String from;
    private String to;
    private String gate;
    private Itinerary departureTrip;
    private Itinerary returnTrip;
    @JsonProperty("tariff")
    private TariffDTO tariffDTO;
    private Currency currency;
    private FlightStatus status;
}
