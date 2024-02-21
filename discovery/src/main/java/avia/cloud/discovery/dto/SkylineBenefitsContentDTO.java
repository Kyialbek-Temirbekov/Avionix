package avia.cloud.discovery.dto;

import avia.cloud.discovery.entity.enums.Lan;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SkylineBenefitsContentDTO {
    private String title;
    private String description;
}
