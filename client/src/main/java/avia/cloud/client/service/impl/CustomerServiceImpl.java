package avia.cloud.client.service.impl;

import avia.cloud.client.dto.ClientDetails;
import avia.cloud.client.dto.CustomerDTO;
import avia.cloud.client.dto.VerificationInfo;
import avia.cloud.client.entity.AccountBase;
import avia.cloud.client.entity.Customer;
import avia.cloud.client.entity.enums.Role;
import avia.cloud.client.exception.NotFoundException;
import avia.cloud.client.repository.CustomerRepository;
import avia.cloud.client.service.ICustomerService;
import avia.cloud.client.service.MessagingService;
import avia.cloud.client.util.NumericTokenGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomerServiceImpl implements ICustomerService {
    private final MessagingService messagingService;
    private final CustomerRepository customerRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
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
        messagingService.sendMessage(customerDTO.getEmail(),"Email Verification", code + " - this is verification code. Use it to sign up to Cloud Ticket Airlines.");
    }

    @Override
    public void confirmEmail(VerificationInfo verificationInfo) {
        Customer customer = customerRepository.findByEmail(verificationInfo.getEmail())
                .orElseThrow(() -> new NotFoundException("Customer","email", verificationInfo.getEmail()));
        if(!customer.getCode().equals(verificationInfo.getCode())) {
            throw new BadCredentialsException("Invalid code: " + verificationInfo.getCode());
        }
        customer.setEnabled(true);
        customer.setCode(null);
        customerRepository.save(customer);
        Authentication auth = new UsernamePasswordAuthenticationToken(customer.getEmail(), null,
                AuthorityUtils.commaSeparatedStringToAuthorityList(
                        customer.getRoles().stream().map(Enum::toString).collect(Collectors.joining(","))
                ));
        SecurityContextHolder.getContextHolderStrategy().createEmptyContext().setAuthentication(auth);
    }

    private Customer convertToCustomer(CustomerDTO customerDTO) {
        return modelMapper.map(customerDTO, Customer.class);
    }
    private ClientDetails convertToClientDetails(Customer customer) {
        return modelMapper.map(customer, ClientDetails.class);
    }
    private ClientDetails convertToClientDetails(AccountBase account) {
        return modelMapper.map(account, ClientDetails.class);
    }

    private CustomerDTO convertToCustomerDTO(Customer customer) {
        return modelMapper.map(customer, CustomerDTO.class);
    }

}
