package avia.cloud.flight.dto;

import avia.cloud.flight.entity.enums.Cabin;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TariffDTO {
    private Cabin cabin;
    private double price;
    private double baggagePrice;
    private short discount;
}
