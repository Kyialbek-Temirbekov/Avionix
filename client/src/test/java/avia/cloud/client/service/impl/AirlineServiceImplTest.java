package avia.cloud.client.service.impl;

import avia.cloud.client.dto.AirlineDTO;
import avia.cloud.client.dto.VerificationInfo;
import avia.cloud.client.entity.Airline;
import avia.cloud.client.exception.NotFoundException;
import avia.cloud.client.repository.AirlineRepository;
import avia.cloud.client.security.TokenGenerator;
import avia.cloud.client.service.IAirlineService;
import avia.cloud.client.util.ClientCredentialGenerator;
import avia.cloud.client.util.Messenger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class AirlineServiceImplTest {
    private IAirlineService iAirlineService;
    @Mock
    private AirlineRepository airlineRepository;
    @MockBean
    private Messenger messenger;
    @MockBean
    private ModelMapper modelMapper;
    @MockBean
    private PasswordEncoder passwordEncoder;
    @MockBean
    private ClientCredentialGenerator clientCredentialGenerator;
    @MockBean
    private TokenGenerator tokenGenerator;

    @BeforeEach
    void setUp() {
        this.iAirlineService = new AirlineServiceImpl(messenger,airlineRepository,modelMapper,passwordEncoder,clientCredentialGenerator,tokenGenerator);
    }

    @Test
    void willThrowWhenAccountNotFoundByClientId() {
        given(airlineRepository.findByClientId(any())).willReturn(Optional.empty());
        assertThatThrownBy(() -> iAirlineService.createAirline(new AirlineDTO()))
                .isInstanceOf(NotFoundException.class);
    }
    @Test
    void willThrowWhenAccountNotFoundByEmail() {
        given(airlineRepository.findByEmail(any())).willReturn(Optional.empty());
        assertThatThrownBy(() -> iAirlineService.confirmEmail(new VerificationInfo("email","2211")))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void willThrowIfClientCredentialsIsInvalid() {
        given(airlineRepository.findByClientId(any())).willReturn(Optional.of(airline()));
        assertThatThrownBy(() -> iAirlineService.createAirline(airlineDTO()))
                .isInstanceOf(BadCredentialsException.class);
    }

    @Test
    void willThrowWhenVerificationCodeIsInvalid() {
        given(airlineRepository.findByEmail(any())).willReturn(Optional.of(airline()));
        assertThatThrownBy(() -> iAirlineService.confirmEmail(new VerificationInfo("email","2211")))
                .isInstanceOf(BadCredentialsException.class);
    }

    Airline airline() {
        return Airline.builder()
                .clientId("1")
                .clientSecret("adf")
                .code("1122").build();
    }
    AirlineDTO airlineDTO() {
        return AirlineDTO.builder()
                .clientId("1")
                .clientSecret("adb").build();
    }
}