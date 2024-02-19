package avia.cloud.client.service.impl;

import avia.cloud.client.dto.*;
import avia.cloud.client.entity.AccountBase;
import avia.cloud.client.entity.Customer;
import avia.cloud.client.entity.enums.Role;
import avia.cloud.client.exception.NotFoundException;
import avia.cloud.client.repository.AirlineRepository;
import avia.cloud.client.repository.CustomerRepository;
import avia.cloud.client.security.TokenGenerator;
import avia.cloud.client.service.ICustomerService;
import avia.cloud.client.util.Messenger;
import avia.cloud.client.util.NumericTokenGenerator;
import avia.cloud.client.util.RoleConverter;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomerServiceImpl implements ICustomerService {
    private final Messenger messenger;
    private final CustomerRepository customerRepository;
    private final AirlineRepository airlineRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final TokenGenerator tokenGenerator;

    @Override
    public void createCustomer(CustomerDTO customerDTO) {
        String code = NumericTokenGenerator.generateToken(6);
        Customer customer = convertToCustomer(customerDTO);
        customer.setRoles(Arrays.asList(Role.CLIENT));
        customer.setEnabled(false);
        customer.setNonLocked(true);
        customer.setCode(code);
        customer.setPassword(passwordEncoder.encode(customerDTO.getPassword()));
        customerRepository.save(customer);
        messenger.sendSimpleMessage(new SimpleMailMessageDTO(
                        customerDTO.getEmail(),
                        "Email Verification",
                        code + " - This is verification code. Use it to sign up to Avionix Airline."
                ));
    }

    @Override
    public void createCustomerOAuth(CustomerDTO customerDTO) {
        Customer customer = convertToCustomer(customerDTO);
        customer.setRoles(Arrays.asList(Role.CLIENT));
        customer.setEnabled(true);
        customer.setNonLocked(true);
        customerRepository.save(customer);
    }

    @Override
    public Authorization confirmEmail(VerificationInfo verificationInfo) {
        Customer customer = customerRepository.findByEmail(verificationInfo.getEmail())
                .orElseThrow(() -> new NotFoundException("Customer","email", verificationInfo.getEmail()));
//        if(!customer.getCode().equals(verificationInfo.getCode())) {
//            throw new BadCredentialsException("Invalid code: " + verificationInfo.getCode());
//        }
        customer.setEnabled(true);
        customer.setCode(null);
        customerRepository.save(customer);
        return tokenGenerator.generate(customer.getEmail(), customer.getRoles()
                .stream().map(Enum::toString).map(RoleConverter::convert).collect(Collectors.joining(",")));
    }

    @Override
    public CustomerDTO fetchCustomer(String email) {
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Customer","email", email));
        return convertToCustomerDTO(customer);
    }

    @Override
    public AccountBase fetchAccount(String email) {
        return  airlineRepository.findByEmailAndEnabledTrue(email).map(airline -> (AccountBase)airline)
                .or(() -> customerRepository.findByEmailAndEnabledTrue(email).map(customer -> (AccountBase)customer))
                .orElseThrow(() -> new UsernameNotFoundException("User details not found for user: " + email));
    }

    @Override
    public void removeAll() {
        customerRepository.deleteAll();
    }

    private Customer convertToCustomer(CustomerDTO customerDTO) {
        return modelMapper.map(customerDTO, Customer.class);
    }
    private ClientDetails convertToClientDetails(Customer customer) {
        return modelMapper.map(customer, ClientDetails.class);
    }

    private CustomerDTO convertToCustomerDTO(Customer customer) {
        return modelMapper.map(customer, CustomerDTO.class);
    }

}
