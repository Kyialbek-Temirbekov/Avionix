package avia.cloud.client;

import avia.cloud.client.filter.AuthenticationFilter;
import avia.cloud.client.filter.RefreshTokenSupplierHandler;
import avia.cloud.client.repository.AirlineRepository;
import avia.cloud.client.security.JwtService;
import avia.cloud.client.util.ClientCredentialGenerator;
import avia.cloud.client.util.Messenger;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

@TestConfiguration
@Profile("test")
public class TestConfig {
    @MockBean
    private JwtService jwtService;
    @MockBean
    private AuthenticationFilter authenticationFilter;
    @MockBean
    private ClientCredentialGenerator clientCredentialGenerator;
    @MockBean
    private RefreshTokenSupplierHandler refreshTokenSupplierHandler;
    @MockBean
    private AirlineRepository airlineRepository;
    @MockBean
    private Messenger messenger;
    @MockBean
    private ModelMapper modelMapper;
    @MockBean
    private PasswordEncoder passwordEncoder;
}

