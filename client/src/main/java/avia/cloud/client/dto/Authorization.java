package avia.cloud.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Authorization {
    private String tokenType;
    private String accessToken;
    private int accessExpiresIn;
    private String refreshToken;
    private int refreshExpiresIn;
    private String roles;
}
