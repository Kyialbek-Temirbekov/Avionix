package avia.cloud.client.service;

import avia.cloud.client.dto.ClientDetails;
import avia.cloud.client.dto.CustomerDTO;
import avia.cloud.client.entity.AccountBase;

public interface ICustomerService extends IClientDetailsService{
    void createCustomer(CustomerDTO customerDTO);
    CustomerDTO fetchCustomer(String email);
    AccountBase fetchAccount(String email);
    void createCustomerOAuth(String email);
}
