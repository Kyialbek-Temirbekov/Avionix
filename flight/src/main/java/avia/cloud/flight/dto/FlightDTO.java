package avia.cloud.flight.dto;

import avia.cloud.flight.entity.Segment;
import avia.cloud.flight.entity.Tariff;
import avia.cloud.flight.entity.enums.Currency;
import avia.cloud.flight.entity.enums.FlightStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlightDTO {
    private List<Segment> segments;
    private boolean oneWay;
    private String from;
    private String to;
    private String gate;
    private List<Tariff> tariffs;
    private Currency currency;
    private FlightStatus status;
}
