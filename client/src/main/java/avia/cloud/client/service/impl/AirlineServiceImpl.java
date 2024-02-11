package avia.cloud.client.service.impl;

import avia.cloud.client.dto.AirlineDTO;
import avia.cloud.client.dto.ClientCredentials;
import avia.cloud.client.dto.ClientDetails;
import avia.cloud.client.dto.VerificationInfo;
import avia.cloud.client.entity.Airline;
import avia.cloud.client.entity.enums.Role;
import avia.cloud.client.exception.NotFoundException;
import avia.cloud.client.repository.AirlineRepository;
import avia.cloud.client.service.IAirlineService;
import avia.cloud.client.service.MessagingService;
import avia.cloud.client.util.ClientCredentialGenerator;
import avia.cloud.client.util.NumericTokenGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@Transactional
@RequiredArgsConstructor
public class AirlineServiceImpl implements IAirlineService {
    private final MessagingService messagingService;
    private final AirlineRepository airlineRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final ClientCredentialGenerator clientCredentialGenerator;
    @Override
    public ClientCredentials createClient(String name) {
        Airline airline = Airline.builder()
                        .clientId(clientCredentialGenerator.generateId(name))
                        .clientSecret(clientCredentialGenerator.generateSecret(name))
                        .build();
        airlineRepository.save(airline);
        return new ClientCredentials(airline.getClientId(), airline.getClientSecret());
    }

    @Override
    public AirlineDTO createAirline(AirlineDTO airlineDTO) {
        Airline airlineData = airlineRepository.findByClientId(airlineDTO.getClientId())
                .orElseThrow(() -> new NotFoundException("Airline","clientId",airlineDTO.getClientId()));
        if (!airlineData.getClientSecret().equals(airlineDTO.getClientSecret())) {
            throw new BadCredentialsException("Invalid client secret: " + airlineDTO.getClientSecret());
        }
        String code = NumericTokenGenerator.generateToken(6);
        Airline airline = convertToAirline(airlineDTO);
        airline.setCode(code);
        airline.setRoles(Arrays.asList(Role.OWNER));
        airline.setEnabled(false);
        airline.setNonLocked(true);
        airline.setPassword(passwordEncoder.encode(airlineDTO.getPassword()));
        airline.setId(airlineData.getId());
        airlineRepository.save(airline);
        messagingService.sendMessage(airlineDTO.getEmail(),"Email Verification", code + " - this is verification code. Use it to sign up to Cloud Ticket Airlines.");
        return convertToAirlineDTO(airline);
    }

    @Override
    public void confirmEmail(VerificationInfo verificationInfo) {
        Airline airline = airlineRepository.findByEmail(verificationInfo.getEmail())
                .orElseThrow(() -> new NotFoundException("Airline","email", verificationInfo.getEmail()));
        if(!airline.getCode().equals(verificationInfo.getCode())) {
            throw new BadCredentialsException("Invalid code: " + verificationInfo.getCode());
        }
        airline.setEnabled(true);
        airline.setCode(null);
        airlineRepository.save(airline);
    }

    private ClientDetails convertToClientDetails(Airline airline) {
        return modelMapper.map(airline, ClientDetails.class);
    }
    private Airline convertToAirline(AirlineDTO airlineDTO) {
        return modelMapper.map(airlineDTO, Airline.class);
    }
    private AirlineDTO convertToAirlineDTO(Airline airline) {
        return modelMapper.map(airline, AirlineDTO.class);
    }
    private ClientCredentials convertToClientCredentials(Airline airline) {
        return modelMapper.map(airline, ClientCredentials.class);
    }

}
