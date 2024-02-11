package avia.cloud.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AirlineDTO extends AccountBaseDTO{
    @JsonProperty("IATA")
    private String IATA;
    private String name;
    private byte[] logo;
    private String address;
    private String officialWebsiteUrl;
    private String description;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String clientId;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String clientSecret;
}
