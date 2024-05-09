package avia.cloud.client.service.impl;

import avia.cloud.client.dto.Authorization;
import avia.cloud.client.dto.PasswordResetInfo;
import avia.cloud.client.dto.SimpleMailMessageDTO;
import avia.cloud.client.dto.VerificationInfo;
import avia.cloud.client.dto.management.AccountMDTO;
import avia.cloud.client.entity.Account;
import avia.cloud.client.entity.Customer;
import avia.cloud.client.exception.NotFoundException;
import avia.cloud.client.repository.AccountRepository;
import avia.cloud.client.security.JwtService;
import avia.cloud.client.service.IAccountService;
import avia.cloud.client.util.AuthorityUtils;
import avia.cloud.client.util.Messenger;
import avia.cloud.client.util.NumericTokenGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountServiceImpl implements IAccountService {
    private final Messenger messenger;
    private final AccountRepository accountRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @Override
    public Authorization confirmEmail(VerificationInfo verificationInfo) {
        Account user = accountRepository.findFirstByEmailOrderByCreatedAtDesc(verificationInfo.getEmail())
                .orElseThrow(() -> new NotFoundException("User","email", verificationInfo.getEmail()));
        if(!user.getCode().equals(verificationInfo.getCode())) {
            throw new BadCredentialsException("Invalid code: " + verificationInfo.getCode());
        }
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

    @Override
    public String findAccountId(String email) {
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Account","email", email));
        return account.getId();
    }

    @Override
    public void forgotPassword(String email) {
        Account account = accountRepository.findByEmail(email).orElseThrow(() ->
                new NotFoundException("Account", "email", email));
        String code = NumericTokenGenerator.generateToken(6);
        account.setCode(code);
        accountRepository.save(account);
        messenger.sendSimpleMessage(new SimpleMailMessageDTO(
                account.getEmail(),
                "Email Verification",
                code + " - This is verification code. Use it to sign up to Avionix Airline."
        ));
    }

    @Override
    public void resetPassword(PasswordResetInfo passwordResetInfo) {
        Account account = accountRepository.findByEmailAndEnabledTrue(passwordResetInfo.getEmail())
                .orElseThrow(() -> new NotFoundException("Account","email", passwordResetInfo.getEmail()));
        if(!account.getCode().equals(passwordResetInfo.getCode())) {
            throw new BadCredentialsException("Invalid code: " + passwordResetInfo.getCode());
        }
        account.setCode(null);
        account.setPassword(passwordEncoder.encode(passwordResetInfo.getPassword()));
        accountRepository.save(account);
    }

    @Override
    public List<AccountMDTO> fetchAllAccounts() {
        return accountRepository.findAll().stream().map(this::convertToAccountMDTO).toList();
    }

    @Override
    public void lockAccount(String id) {
        Account account = accountRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Account","id", id));
        account.setNonLocked(false);
        accountRepository.save(account);
    }
    @Override
    public void unlockAccount(String id) {
        Account account = accountRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Account","id", id));
        account.setNonLocked(true);
        accountRepository.save(account);
    }

    private AccountMDTO convertToAccountMDTO(Account account) {
        return modelMapper.map(account,AccountMDTO.class);
    }
}
