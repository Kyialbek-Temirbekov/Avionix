package avia.cloud.client.service;

import avia.cloud.client.dto.CustomerDTO;

public interface ICustomerService extends IClientDetailsService{
    CustomerDTO createCustomer(CustomerDTO customerDTO);
}
