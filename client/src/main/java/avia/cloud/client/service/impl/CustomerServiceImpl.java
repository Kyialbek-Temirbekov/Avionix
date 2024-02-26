package avia.cloud.client.service.impl;

import avia.cloud.client.dto.*;
import avia.cloud.client.entity.Account;
import avia.cloud.client.entity.Customer;
import avia.cloud.client.entity.enums.Role;
import avia.cloud.client.exception.NotFoundException;
import avia.cloud.client.repository.CustomerRepository;
import avia.cloud.client.repository.AccountRepository;
import avia.cloud.client.service.ICustomerService;
import avia.cloud.client.util.ImageUtils;
import avia.cloud.client.util.Messenger;
import avia.cloud.client.util.NumericTokenGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomerServiceImpl implements ICustomerService {
    private final Messenger messenger;
    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

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
    public void createCustomerOAuth(CustomerDTO customerDTO) {
        Customer customer = convertToCustomer(customerDTO);
        Account user = convertToAccount(customerDTO.getAccount());
        user.setRoles(Arrays.asList(Role.CLIENT));
        user.setEnabled(true);
        user.setNonLocked(true);
        customer.setAccount(user);
        customerRepository.save(customer);
    }

    @Override
    public CustomerDTO fetchCustomer(String email) throws IOException {
        Customer customer = accountRepository.findByEmail(email).map(Account::getCustomer)
                .orElseThrow(() -> new NotFoundException("Customer","email", email));
        return convertToCustomerDTO(customer);
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
