package avia.cloud.client.dto;

import avia.cloud.client.entity.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClientDetails {
    private String username;
    private List<Role> roles;
    private boolean enabled;
    private boolean nonLocked;
}
