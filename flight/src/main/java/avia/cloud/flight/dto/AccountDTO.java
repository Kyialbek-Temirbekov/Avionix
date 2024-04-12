package avia.cloud.flight.dto;

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
    private String id;
    @Email
    @Size(max = 50)
    private String email;
    @NotNull
    @NotBlank
    @Size(max = 50)
    @Pattern(regexp="\\+\\d+", message="Phone number must start with '+' and contain only digits")
    private String phone;
    private String imageUrl;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private MultipartFile multipartFile;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Size(max = 50, min = 5)
    private String password;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String confirmPassword;
    @AssertTrue
    private boolean agreedToTermsOfUse;
}
