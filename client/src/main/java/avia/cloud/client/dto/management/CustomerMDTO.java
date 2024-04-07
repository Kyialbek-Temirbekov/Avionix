package avia.cloud.client.dto.management;

import avia.cloud.client.entity.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerMDTO {
    private String baseId;
    private String firstName;
    private String lastName;
    private Gender gender;
    private LocalDate dateOfBirth;
    private String nationality;
    private String passportId;
    private LocalDate passportExpiryDate;
    String imageUrl;
}