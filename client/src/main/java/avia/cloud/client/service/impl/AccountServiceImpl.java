package avia.cloud.client.service.impl;

import avia.cloud.client.dto.Authorization;
import avia.cloud.client.dto.VerificationInfo;
import avia.cloud.client.entity.Account;
import avia.cloud.client.exception.NotFoundException;
import avia.cloud.client.repository.AccountRepository;
import avia.cloud.client.security.JwtService;
import avia.cloud.client.service.IAccountService;
import avia.cloud.client.util.AuthorityUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountServiceImpl implements IAccountService {
    private final AccountRepository accountRepository;
    private final JwtService jwtService;
    @Override
    public Authorization confirmEmail(VerificationInfo verificationInfo) {
        Account user = accountRepository.findFirstByEmailOrderByCreatedAtDesc(verificationInfo.getEmail())
                .orElseThrow(() -> new NotFoundException("User","email", verificationInfo.getEmail()));
//        if(!user.getCode().equals(verificationInfo.getCode())) {
//            throw new BadCredentialsException("Invalid code: " + verificationInfo.getCode());
//        }
        user.setEnabled(true);
        user.setCode(null);
        accountRepository.save(user);
        return jwtService.createToken(user.getEmail(), user.getRoles()
                .stream().map(Enum::toString).map(AuthorityUtils::addPrefix).collect(Collectors.joining(",")));
    }
    @Override
    public Account fetchUser(String email) {
        return  accountRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User details not found for user: " + email));
    }

    @Override
    public Authorization signIn(Authentication authentication) {
        return jwtService.createToken(authentication.getName(), populateAuthorities(authentication.getAuthorities()));
    }

    @Override
    public void removeAll() {
        accountRepository.deleteAllInBatch();
    }

    private String populateAuthorities(Collection<? extends GrantedAuthority> authorities) {
        Set<String> authoritiesSet = new HashSet<>();
        for(GrantedAuthority authority : authorities)
            if (authority.getAuthority().startsWith("ROLE_")) {
                authoritiesSet.add(authority.getAuthority());
            }
        return String.join(",", authoritiesSet);
    }
}
