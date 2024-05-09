package avia.cloud.client.service.impl;

import avia.cloud.client.dto.Authorization;
import avia.cloud.client.dto.VerificationInfo;
import avia.cloud.client.entity.Account;
import avia.cloud.client.entity.enums.Role;
import avia.cloud.client.exception.NotFoundException;
import avia.cloud.client.repository.AccountRepository;
import avia.cloud.client.security.JwtService;
import avia.cloud.client.service.IAccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {
    @Mock
    private AccountRepository accountRepository;
    private JwtService jwtService;
    private IAccountService accountService;

    @BeforeEach
    void setUp() throws IllegalAccessException, NoSuchFieldException {
        this.jwtService = new JwtService();
        this.accountService = new AccountServiceImpl(null, accountRepository, jwtService, null, null);
        Field jwtKeyField = JwtService.class.getDeclaredField("jwtKey");
        jwtKeyField.setAccessible(true);
        jwtKeyField.set(jwtService, "891b95720fcaccc91ae9d555f843b69b6847f41ed73a561755f1fa");
    }

    @Test
    void willThrowWhenVerificationCodeIsInvalid() {
        given(accountRepository.findFirstByEmailOrderByCreatedAtDesc(any())).willReturn(Optional.of(account()));
        assertThatThrownBy(() -> accountService.confirmEmail(new VerificationInfo("email","000")))
                .isInstanceOf(BadCredentialsException.class);
    }

    @Test
    void willThrowWhenNotFoundByEmail() {
        given(accountRepository.findFirstByEmailOrderByCreatedAtDesc(any())).willReturn(Optional.empty());
        assertThatThrownBy(() -> accountService.confirmEmail(new VerificationInfo("email","000")))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void willReturnAccessAndRefreshToken() {
        given(accountRepository.findFirstByEmailOrderByCreatedAtDesc(any())).willReturn(Optional.of(account()));
        var outcome = accountService.confirmEmail(new VerificationInfo("email","111"));
        assertEquals(Authorization.class, outcome.getClass());
        assertNotNull(outcome);
    }

    @Test
    void willThrowWhenUsernameNotFound() {
        given(accountRepository.findByEmail(any())).willReturn(Optional.empty());
        assertThatThrownBy(() -> accountService.fetchUser("email"))
                .isInstanceOf(UsernameNotFoundException.class);
    }

    Account account() {
        return Account.builder()
                .code("111")
                .roles(Arrays.asList(Role.CLIENT)).build();
    }
}