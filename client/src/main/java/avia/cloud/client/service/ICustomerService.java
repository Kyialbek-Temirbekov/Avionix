package avia.cloud.client.service;

import avia.cloud.client.dto.CustomerDTO;

import java.io.IOException;

public interface ICustomerService {
    void createCustomer(CustomerDTO customerDTO) throws IOException;
    CustomerDTO fetchCustomer(String email);
    void createCustomerOAuth(CustomerDTO customerDTO);
}
