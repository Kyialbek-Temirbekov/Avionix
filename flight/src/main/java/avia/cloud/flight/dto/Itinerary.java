package avia.cloud.flight.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Itinerary {
    private long flightDuration;
    private long transitDuration;
    private List<SegmentDTO> segments;
}
