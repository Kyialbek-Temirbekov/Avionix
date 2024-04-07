package avia.cloud.flight.dto;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CityRequestDTO {
    @Pattern(regexp = "^[A-Z]{2}-?[A-Z]{2}$", message = "Invalid city code")
    private String code;
}
