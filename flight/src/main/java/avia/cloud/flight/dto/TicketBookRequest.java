package avia.cloud.flight.dto;

import avia.cloud.flight.validation.constraint.Vacant;
import avia.cloud.flight.validation.constraint.ValidSeat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketBookRequest {
    private String flightId;
    @Vacant
    @NotNull
    private String seat;
    private boolean checkedBaggageIncluded;
    private String paymentLinkId;
}
