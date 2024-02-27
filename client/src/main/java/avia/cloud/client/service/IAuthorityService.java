package avia.cloud.client.service;

import avia.cloud.client.dto.AuthorityDTO;
import avia.cloud.client.entity.Authority;
import avia.cloud.client.entity.enums.Role;

import java.util.List;

public interface IAuthorityService {
    List<AuthorityDTO> fetchAuthorities(List<Role> role);
}
