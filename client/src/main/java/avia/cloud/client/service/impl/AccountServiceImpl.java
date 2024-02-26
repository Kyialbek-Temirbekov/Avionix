package avia.cloud.client.service.impl;

import avia.cloud.client.dto.Authorization;
import avia.cloud.client.dto.VerificationInfo;
import avia.cloud.client.entity.Account;
import avia.cloud.client.exception.NotFoundException;
import avia.cloud.client.repository.AccountRepository;
import avia.cloud.client.security.TokenGenerator;
import avia.cloud.client.service.IAccountService;
import avia.cloud.client.util.RoleConverter;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountServiceImpl implements IAccountService {
    private final AccountRepository accountRepository;
    private final TokenGenerator tokenGenerator;
    @Override
    public Authorization confirmEmail(VerificationInfo verificationInfo) {
        Account user = accountRepository.findByEmail(verificationInfo.getEmail())
                .orElseThrow(() -> new NotFoundException("User","email", verificationInfo.getEmail()));
//        if(!user.getCode().equals(verificationInfo.getCode())) {
//            throw new BadCredentialsException("Invalid code: " + verificationInfo.getCode());
//        }
        user.setEnabled(true);
        user.setCode(null);
        accountRepository.save(user);
        return tokenGenerator.generate(user.getEmail(), user.getRoles()
                .stream().map(Enum::toString).map(RoleConverter::convert).collect(Collectors.joining(",")));
    }
    @Override
    public Account fetchUser(String email) {
        return  accountRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User details not found for user: " + email));
    }
    @Override
    public void removeAll() {
        accountRepository.deleteAllInBatch();
    }
}
