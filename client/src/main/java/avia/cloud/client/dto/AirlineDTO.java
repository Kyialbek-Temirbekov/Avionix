package avia.cloud.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AirlineDTO {
    private String iata;
    private String name;
    private String address;
    private String officialWebsiteUrl;
    private String description;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String clientId;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String clientSecret;
    @JsonUnwrapped
    private AccountDTO account;
}
