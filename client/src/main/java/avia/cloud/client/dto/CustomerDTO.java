package avia.cloud.client.dto;

import avia.cloud.client.entity.enums.Gender;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDTO {
    private String firstName;
    private String lastName;
    private Gender gender;
    private LocalDate dateOfBirth;
    private String nationality;
    private String passportId;
    private LocalDate passportExpiryDate;
    @JsonUnwrapped
    private AccountDTO account;
}
