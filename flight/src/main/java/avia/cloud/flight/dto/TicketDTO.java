package avia.cloud.flight.dto;

import avia.cloud.flight.entity.enums.TicketStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TicketDTO {
    private CustomerDTO customer;
    private String seat;
    private boolean checkedBaggageIncluded;
    private TicketStatus status;
    private FlightDTO flight;
}
