package avia.cloud.client.dto.management;

import avia.cloud.client.entity.enums.Role;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountMDTO {
    private String id;
    private String email;
    private String phone;
    private List<Role> roles;
    private boolean enabled;
    private boolean nonLocked;
    private boolean agreedToTermsOfUse;
    private String code;
}
