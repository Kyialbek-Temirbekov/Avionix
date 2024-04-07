package avia.cloud.client.service.impl;

import avia.cloud.client.dto.AuthorityDTO;
import avia.cloud.client.entity.Authority;
import avia.cloud.client.entity.enums.Role;
import avia.cloud.client.repository.AuthorityRepository;
import avia.cloud.client.service.IAuthorityService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthorityServiceImpl implements IAuthorityService {
    private final AuthorityRepository authorityRepository;
    private final ModelMapper modelMapper;
    @Override
    public List<AuthorityDTO> fetchAuthorities(List<Role> roles) {
        return authorityRepository.findAllByRoleIn(roles).stream().map(this::convertToAuthorityDTO).toList();
    }

    private AuthorityDTO convertToAuthorityDTO(Authority authority) {
        return modelMapper.map(authority, AuthorityDTO.class);
    }
}
