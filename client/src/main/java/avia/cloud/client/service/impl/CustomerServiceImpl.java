package avia.cloud.client.service.impl;

import avia.cloud.client.dto.*;
import avia.cloud.client.entity.Account;
import avia.cloud.client.entity.Customer;
import avia.cloud.client.entity.enums.Role;
import avia.cloud.client.exception.NotFoundException;
import avia.cloud.client.repository.CustomerRepository;
import avia.cloud.client.repository.AccountRepository;
import avia.cloud.client.security.JwtService;
import avia.cloud.client.service.ICustomerService;
import avia.cloud.client.util.AuthorityUtils;
import avia.cloud.client.util.ImageUtils;
import avia.cloud.client.util.Messenger;
import avia.cloud.client.util.NumericTokenGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomerServiceImpl implements ICustomerService {
    private final Messenger messenger;
    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public void createCustomer(CustomerDTO customerDTO) throws IOException {
        String code = NumericTokenGenerator.generateToken(6);
        MultipartFile multipartFile = customerDTO.getAccount().getMultipartFile();
        byte[] image = null;
        if(multipartFile!=null && !multipartFile.isEmpty()) {
            image = multipartFile.getBytes();
        }

        Customer customer = convertToCustomer(customerDTO);
        Account account = convertToAccount(customerDTO.getAccount());
        account.setRoles(Arrays.asList(Role.CLIENT));
        account.setEnabled(false);
        account.setNonLocked(true);
        account.setCode(code);
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        account.setImage(image);
        customer.setAccount(account);
        accountRepository.save(account);
        customerRepository.save(customer);
        messenger.sendSimpleMessage(new SimpleMailMessageDTO(
                        account.getEmail(),
                        "Email Verification",
                        code + " - This is verification code. Use it to sign up to Avionix Airline."
                ));
    }

    @Override
    public Authorization recordCustomer(CustomerDTO customerDTO) {
        Customer customer = convertToCustomer(customerDTO);
        Account account = convertToAccount(customerDTO.getAccount());
        account.setRoles(Arrays.asList(Role.CLIENT));
        account.setEnabled(true);
        account.setNonLocked(true);
        customer.setAccount(account);
        customerRepository.save(customer);
        return jwtService.createToken(account.getEmail(), account.getRoles()
                .stream().map(Enum::toString).map(AuthorityUtils::addPrefix).collect(Collectors.joining(",")));
    }

    @Override
    public CustomerDTO fetchCustomer(String email) throws IOException {
        Customer customer = accountRepository.findByEmail(email).map(Account::getCustomer)
                .orElseThrow(() -> new NotFoundException("Customer","email", email));
        return convertToCustomerDTO(customer);
    }

    @Override
    public Authorization oauthSignIn(Authentication authentication) {
        String email = authentication.getName();
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Customer","email", email));
        if (!account.isNonLocked()) {
            throw new LockedException("Customer's account is locked");
        }
        return jwtService.createToken(email, account.getRoles()
                .stream().map(Enum::toString).map(AuthorityUtils::addPrefix).collect(Collectors.joining(",")));
    }

    @Override
    public String findCustomerId(String email) {
        Customer customer = accountRepository.findByEmail(email).map(Account::getCustomer)
                .orElseThrow(() -> new NotFoundException("Customer","email", email));
        return customer.getBaseId();
    }

    private Account convertToAccount(AccountDTO accountDTO) {
        return modelMapper.map(accountDTO, Account.class);
    }
    private Customer convertToCustomer(CustomerDTO customerDTO) {
        return modelMapper.map(customerDTO, Customer.class);
    }

    private CustomerDTO convertToCustomerDTO(Customer customer) throws IOException {
        CustomerDTO customerDTO = modelMapper.map(customer, CustomerDTO.class);
        byte[] image = customer.getAccount().getImage();
        if(image != null) {
            customerDTO.getAccount().setImageUrl(ImageUtils.getBase64Image(image));
        }
        return customerDTO;
    }

}
