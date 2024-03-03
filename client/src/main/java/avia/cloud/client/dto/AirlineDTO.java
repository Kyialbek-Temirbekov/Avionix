package avia.cloud.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AirlineDTO {
    @NotNull
    @NotBlank
    @Size(min = 3, max = 3)
    private String iata;
    @NotNull
    @NotBlank
    @Size(max = 50)
    private String name;
    @NotNull
    @NotBlank
    @Size(max = 50)
    private String address;
    private String officialWebsiteUrl;
    @NotNull
    @NotBlank
    private String description;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull
    @NotBlank
    @Size(max = 50)
    private String clientId;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull
    @NotBlank
    @Size(max = 100)
    private String clientSecret;
    @JsonUnwrapped
    @Valid
    private AccountDTO account;
}
