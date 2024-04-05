package avia.cloud.client.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class PasswordResetInfo extends VerificationInfo {
    private String password;
    private String confirmPassword;
}
