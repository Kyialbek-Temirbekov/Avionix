package avia.cloud.client.service.impl;

import avia.cloud.client.repository.AirlineRepository;
import avia.cloud.client.repository.CustomerRepository;
import avia.cloud.client.security.JwtService;
import avia.cloud.client.service.ICustomerService;
import avia.cloud.client.util.Messenger;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {
    private ICustomerService iCustomerService;
    @Mock
    private CustomerRepository customerRepository;
    @MockBean
    private AirlineRepository airlineRepository;
    @MockBean
    private Messenger messenger;
    @MockBean
    private ModelMapper modelMapper;
    @MockBean
    private PasswordEncoder passwordEncoder;
    @MockBean
    private JwtService jwtService;

//    @BeforeEach
//    void setUp() {
//        this.iCustomerService = new CustomerServiceImpl(messenger,customerRepository,airlineRepository,modelMapper,passwordEncoder,tokenGenerator);
//    }
//
//    @Test
//    void willThrowWhenVerificationCodeIsInvalid() {
//        given(customerRepository.findByEmail(any())).willReturn(Optional.of(customer()));
//        assertThatThrownBy(() -> iCustomerService.confirmEmail(new VerificationInfo("email","1000")))
//                .isInstanceOf(BadCredentialsException.class);
//    }
//
//    @Test
//    void willThrowWhenNotFoundByEmail() {
//        given(customerRepository.findByEmail(any())).willReturn(Optional.empty());
//        assertThatThrownBy(() -> iCustomerService.confirmEmail(new VerificationInfo("email","1000")))
//                .isInstanceOf(NotFoundException.class);
//    }
//
//    Customer customer() {
//        return Customer.builder()
//                .code("2233").build();
//    }
}