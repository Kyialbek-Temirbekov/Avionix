package avia.cloud.client.service;

import avia.cloud.client.dto.Authorization;
import avia.cloud.client.dto.CustomerDTO;
import org.springframework.security.core.Authentication;

import java.io.IOException;

public interface ICustomerService {
    void createCustomer(CustomerDTO customerDTO) throws IOException;
    CustomerDTO fetchCustomer(String email) throws IOException;
    Authorization recordCustomer(CustomerDTO customerDTO);
    Authorization oauthSignIn(Authentication authentication);
}

