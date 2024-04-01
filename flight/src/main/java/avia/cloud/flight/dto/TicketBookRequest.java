package avia.cloud.flight.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketBookRequest {
    private String flightId;
    private String seat;
    private boolean checkedBaggageIncluded;
    @JsonProperty("charge")
    private ChargeRequest chargeRequest;
}
