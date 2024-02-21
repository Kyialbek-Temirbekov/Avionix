package avia.cloud.discovery.dto;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SkylineBenefitsDTO {
    private byte[] logo;
    @JsonUnwrapped
    private SkylineBenefitsContentDTO content;
}
