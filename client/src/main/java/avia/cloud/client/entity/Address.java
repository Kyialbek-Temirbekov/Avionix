package avia.cloud.client.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    private String cityName;
    private String cityCode;
    private String countryName;
    private String countryCode;
    private String regionCode;
}
