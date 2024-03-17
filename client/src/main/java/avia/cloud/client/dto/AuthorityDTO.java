package avia.cloud.client.dto;

import avia.cloud.client.entity.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorityDTO {
    private String id;
    private Role role;
    private String target;
    private boolean create;
    private boolean read;
    private boolean update;
    private boolean delete;
}
