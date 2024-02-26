package avia.cloud.discovery.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SkylineBenefitsDTO {
    private String id;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private MultipartFile multipartFile;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String logoUrl;
    @JsonUnwrapped
    private SkylineBenefitsContentDTO content;
}
