package avia.cloud.client.service;

import avia.cloud.client.dto.ClientDetails;
import avia.cloud.client.dto.CustomerDTO;

public interface ICustomerService extends IClientDetailsService{
    void createCustomer(CustomerDTO customerDTO);
    CustomerDTO fetchCustomer(String email);
}
