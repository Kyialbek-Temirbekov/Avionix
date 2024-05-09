package avia.cloud.client.controller;

import avia.cloud.client.TestConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestConfig.class)
@ExtendWith(MockitoExtension.class)
@Profile("test")
class CustomerControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void canValidate() throws Exception {
        String customer = "{\n" +
                "    \"email\": \"primary1ta@gmail.com\",\n" +
                "    \"phone\": \"+996990552789\",\n" +
                "    \"password\": \"primary\",\n" +
                "    \"confirmPassword\": \"primary\",\n" +
                "    \"firstName\": \"John\",\n" +
                "    \"lastName\": \"Doe\",\n" +
                "    \"gender\": \"MALE\",\n" +
                "    \"dateOfBirth\": \"2002-02-06\",\n" +
                "    \"nationality\": \"kyrghyz\",\n" +
                "    \"passportId\": \"20112200100455\",\n" +
                "    \"passportExpiryDate\": \"2030-01-12\",\n" +
                "    \"agreedToTermsOfUse\": true,\n" +
                "    \"image\": null\n" +
                "}";
        this.mockMvc.perform(post("/api/customer").contentType(MediaType.APPLICATION_JSON)
                .content(customer)).andDo(print()).andExpect(status().isOk());
    }
    @Test
    void canValidateInvalidFields() throws Exception {
        String customer = "{\n" +
                "    \"email\": \"primary1ta@gmail.com\",\n" +
                "    \"phone\": \"+996990552789\",\n" +
                "    \"password\": \"primary\",\n" +
                "    \"confirmPassword\": \"primary\",\n" +
                "    \"firstName\": \"John\",\n" +
                "    \"lastName\": \"Doe\",\n" +
                "    \"gender\": \"MALE\",\n" +
                "    \"dateOfBirth\": \"2002-02-06\",\n" +
                "    \"nationality\": \"kyrghyz\",\n" +
                "    \"passportId\": \"20112200100455\",\n" +
                "    \"passportExpiryDate\": \"2030-01-12\",\n" +
                "    \"agreedToTermsOfUse\": false,\n" +
                "    \"image\": null\n" +
                "}";
        this.mockMvc.perform(post("/api/customer").contentType(MediaType.APPLICATION_JSON)
                .content(customer)).andDo(print()).andExpect(status().is(400));
    }
}