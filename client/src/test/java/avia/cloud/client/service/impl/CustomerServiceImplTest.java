package avia.cloud.client.service.impl;

import avia.cloud.client.TestConfig;
import avia.cloud.client.dto.VerificationInfo;
import avia.cloud.client.entity.Account;
import avia.cloud.client.entity.Customer;
import avia.cloud.client.repository.AccountRepository;
import avia.cloud.client.repository.CustomerRepository;
import avia.cloud.client.service.IAccountService;
import avia.cloud.client.service.ICustomerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.BadCredentialsException;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestConfig.class)
@ExtendWith(MockitoExtension.class)
@Profile("test")
class CustomerServiceImplTest {
    @Autowired
    private ICustomerService customerService;
    @Autowired
    private IAccountService accountService;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private AccountRepository accountRepository;

//    @BeforeEach
//    void setUp() {
//        this.iCustomerService = new CustomerServiceImpl(messenger,customerRepository,airlineRepository,modelMapper,passwordEncoder,tokenGenerator);
//    }

    @Test
    void willThrowWhenVerificationCodeIsInvalid() {
        given(accountRepository.findFirstByEmailOrderByCreatedAtDesc(any())).willReturn(Optional.of(account()));
        assertThatThrownBy(() -> accountService.confirmEmail(new VerificationInfo("email","000000")))
                .isInstanceOf(BadCredentialsException.class);
    }

//    @Test
//    void willThrowWhenNotFoundByEmail() {
//        given(customerRepository.findByEmail(any())).willReturn(Optional.empty());
//        assertThatThrownBy(() -> iCustomerService.confirmEmail(new VerificationInfo("email","1000")))
//                .isInstanceOf(NotFoundException.class);
//    }

    Account account() {
        return Account.builder()
                .code("111111").build();
    }
}