package avia.cloud.flight.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SegmentDTO {
    private String departureIata;
    private LocalDateTime departureAt;
    private String arrivalIata;
    private LocalDateTime arrivalAt;
}
