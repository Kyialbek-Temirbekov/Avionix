package avia.cloud.client.dto;

import avia.cloud.client.validation.constraint.NotRegistered;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
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
    @NotNull
    @NotBlank
    @Email
    @Size(max = 50)
    @NotRegistered
    private String email;
    @NotNull
    @NotBlank
    @Size(max = 50)
    @Pattern(regexp="\\+\\d+", message="Phone number must start with '+' and contain only digits")
    private String phone;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String imageUrl;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private MultipartFile multipartFile;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull
    @NotBlank
    @Size(max = 50, min = 5)
    private String password;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String confirmPassword;
    @AssertTrue
    private boolean agreedToTermsOfUse;
}
