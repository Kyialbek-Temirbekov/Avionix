package avia.cloud.client.mapper;

import avia.cloud.client.dto.AccountDTO;
import avia.cloud.client.dto.CustomerDTO;
import avia.cloud.client.entity.Account;
import avia.cloud.client.entity.Customer;
import avia.cloud.client.entity.enums.Gender;
import avia.cloud.client.entity.enums.Role;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class AccountMapperTest {
    @Test
    void canMap() throws IOException {
        AccountMapper accountMapper = Mappers.getMapper(AccountMapper.class);
        Account account = Account.builder()
                .id("id")
                .email("example@example.com")
                .phone("1234567890")
                .image(Files.readAllBytes(Paths.get("/home/kyialbek/Downloads/imagecompressor/lot-min.png")))
                .password("password")
                .agreedToTermsOfUse(true)
                .build();
        AccountDTO accountDTO = accountMapper.accountToAccountDTO(account);
        System.out.println(accountDTO);
    }
    @Test
    void canMapEmbed() throws IOException {
        AccountMapper accountMapper = Mappers.getMapper(AccountMapper.class);
        Customer customer = Customer.builder()
                .baseId("baseId")
                .firstName("name")
                .lastName("lastname")
                .gender(Gender.MALE)
                .dateOfBirth(LocalDate.now())
                .nationality("nationality")
                .passportId("44444444444444")
                .passportExpiryDate(LocalDate.now())
                .account(Account.builder()
                        .id("id")
                        .email("example@example.com")
                        .phone("1234567890")
                        .image(Files.readAllBytes(Paths.get("/home/kyialbek/Downloads/imagecompressor/lot-min.png")))
                        .password("password")
                        .agreedToTermsOfUse(true)
                        .build()).build();
        CustomerDTO customerDTO = accountMapper.customerToCustomerDTO(customer);
        System.out.println(customerDTO);
    }
}