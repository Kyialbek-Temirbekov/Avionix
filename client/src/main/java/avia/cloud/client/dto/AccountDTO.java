package avia.cloud.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String id;
    private String email;
    private String phone;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String imageUrl;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private MultipartFile multipartFile;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String confirmPassword;
    private boolean agreedToTermsOfUse;
}
