package avia.cloud.client.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class AddressDTO {
    @NotNull
    @NotBlank
    @Size(max = 50)
    private String cityName;
    @NotNull
    @NotBlank
    @Size(max = 10)
    private String cityCode;
    @NotNull
    @NotBlank
    @Size(max = 50)
    private String countryName;
    @NotNull
    @NotBlank
    @Size(max = 10)
    private String countryCode;
    @NotNull
    @NotBlank
    @Size(max = 10)
    private String regionCode;
}
