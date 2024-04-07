package avia.cloud.client.service.impl;

import avia.cloud.client.dto.*;
import avia.cloud.client.dto.management.AirlineMDTO;
import avia.cloud.client.dto.records.AirlineName;
import avia.cloud.client.dto.records.AirlineRatingRecord;
import avia.cloud.client.entity.Airline;
import avia.cloud.client.entity.Account;
import avia.cloud.client.entity.AirlineRating;
import avia.cloud.client.entity.Customer;
import avia.cloud.client.entity.enums.Role;
import avia.cloud.client.exception.NotFoundException;
import avia.cloud.client.repository.AccountRepository;
import avia.cloud.client.repository.AirlineRatingRepository;
import avia.cloud.client.repository.AirlineRepository;
import avia.cloud.client.service.IAirlineService;
import avia.cloud.client.util.ClientCredentialGenerator;
import avia.cloud.client.util.ImageUtils;
import avia.cloud.client.util.Messenger;
import avia.cloud.client.util.NumericTokenGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AirlineServiceImpl implements IAirlineService {
    private final Messenger messenger;
    private final AirlineRepository airlineRepository;
    private final AccountRepository accountRepository;
    private final AirlineRatingRepository ratingRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final ClientCredentialGenerator clientCredentialGenerator;
    @Override
    public ClientCredentials createClient(String name) {
        Airline airline = Airline.builder()
                        .clientId(clientCredentialGenerator.generateId(name))
                        .clientSecret(clientCredentialGenerator.generateSecret(name))
                        .account(accountRepository.save(new Account()))
                        .build();
        airlineRepository.save(airline);
        return new ClientCredentials(airline.getClientId(), airline.getClientSecret());
    }

    @Override
    public void createAirline(AirlineDTO airlineDTO) throws IOException {
        Airline airline = airlineRepository.findByClientId(airlineDTO.getClientId())
                .orElseThrow(() -> new NotFoundException("Airline","clientId",airlineDTO.getClientId()));
        if (!airline.getClientSecret().equals(airlineDTO.getClientSecret())) {
            throw new BadCredentialsException("Invalid client secret: " + airlineDTO.getClientSecret());
        }
        AccountDTO accountDTO = airlineDTO.getAccount();
        String code = NumericTokenGenerator.generateToken(6);
        MultipartFile multipartFile = accountDTO.getMultipartFile();
        byte[] image = null;
        if(multipartFile!=null && !multipartFile.isEmpty()) {
            image = multipartFile.getBytes();
        }

        Account account = airline.getAccount();
        account.setEmail(accountDTO.getEmail());
        account.setPhone(accountDTO.getPhone());
        account.setImage(image);
        account.setPassword(passwordEncoder.encode(accountDTO.getPassword()));
        account.setAgreedToTermsOfUse(accountDTO.isAgreedToTermsOfUse());
        account.setCode(code);
        account.getRoles().add(Role.OWNER);
        account.setEnabled(false);
        account.setNonLocked(true);
        accountRepository.save(account);

        airline.setIata(airlineDTO.getIata());
        airline.setName(airlineDTO.getName());
        airline.setCityCode(airlineDTO.getCityCode());
        airline.setOfficialWebsiteUrl(airlineDTO.getOfficialWebsiteUrl());
        airline.setDescription(airlineDTO.getDescription());
        airlineRepository.save(airline);

        messenger.sendSimpleMessage(new SimpleMailMessageDTO(
                        account.getEmail(),
                        "Email Verification",
                        code + " - This is verification code. Use it to sign up to Avionix Airline."
                ));
    }

    @Override
    public AirlineDTO fetchAirline(String email) {
        Airline airline = accountRepository.findByEmail(email).map(Account::getAirline)
                .orElseThrow(() -> new NotFoundException("Airline","email", email));
        return convertToAirlineDTO(airline);
    }

    @Override
    public List<AirlineName> findAirlineNames() {
        return airlineRepository.findAllProjectedBy();
    }

    @Override
    public String findAirlineName(String id) {
        return airlineRepository.findById(id).map(Airline::getName).orElseThrow(() ->
                new NotFoundException("Airline","id", id));
    }

    @Override
    public List<AirlineRatingRecord> findAirlineRatings() {
        List<AirlineRating> ratings = ratingRepository.findAirlineRatingBy();
        List<AirlineRatingRecord> ratingRecords = new ArrayList<>();
        short maxGrade = ratings.stream().map(AirlineRating::getGrade).max(Short::compareTo).orElse((short)0);
        ratings.forEach(rating -> {
            Airline airline = rating.getAirline();
            ratingRecords.add(new AirlineRatingRecord(
                    airline.getName(),
                    ImageUtils.getBase64Image(airline.getAccount().getImage()),
                    Double.parseDouble(String.format("%.2f", getRating(rating.getGrade(),maxGrade)))
            ));
        });
        return ratingRecords;
    }

    @Override
    public List<String> findIdsByText(String text) {
        return airlineRepository.findIdsByText(text);
    }

    @Override
    public AirlineMDTO fetchAirlineM(String id) {
        return airlineRepository.findById(id).map(this::convertToAirlineMDTO).orElseThrow(() ->
                new NotFoundException("Airline","id", id));
    }

    private AirlineMDTO convertToAirlineMDTO(Airline airline) {
        AirlineMDTO airlineMDTO = modelMapper.map(airline, AirlineMDTO.class);
        airlineMDTO.setImageUrl(ImageUtils.getBase64Image(airline.getAccount().getImage()));
        return airlineMDTO;
    }

    private double getRating(short grade, short maxGrade) {
        return ((double) 10 / maxGrade) * grade;
    }
    private AirlineDTO convertToAirlineDTO(Airline airline) {
        return modelMapper.map(airline, AirlineDTO.class);
    }

    private Airline convertToAirline(AirlineDTO airlineDTO) {
        return modelMapper.map(airlineDTO, Airline.class);
    }

}
