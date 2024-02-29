package avia.cloud.client.controller;

import avia.cloud.client.dto.AuthorityDTO;
import avia.cloud.client.entity.enums.Role;
import avia.cloud.client.service.IAuthorityService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/authority")
@RequiredArgsConstructor
@Validated
public class AuthorityController {
    private final IAuthorityService iAuthorityService;
    @GetMapping()
    public List<AuthorityDTO> fetchAuthorities(@RequestParam String authorities) {
        return iAuthorityService.fetchAuthorities(Stream.of(authorities.split(",")).map(Role::valueOf).toList());
    }
}
