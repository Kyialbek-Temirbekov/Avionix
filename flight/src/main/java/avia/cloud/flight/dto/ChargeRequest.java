package avia.cloud.flight.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChargeRequest {
    private double amount;
    private String currency;
    private String description;
    private String stripeToken;
}
