package avia.cloud.client.dto.management;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AirlineMDTO {
    private String baseId;
    private String iata;
    private String name;
    private String cityCode;
    private String officialWebsiteUrl;
    private String description;
    private int priority;
    private String imageUrl;
}
