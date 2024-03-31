package avia.cloud.flight.dto;

import avia.cloud.flight.entity.enums.Cabin;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlaneSeatDetail {
    private String make;
    private String model;
    private Cabin cabin;
    private int seatRow;
    private int seatColumn;
}
