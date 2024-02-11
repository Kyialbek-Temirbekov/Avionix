package avia.cloud.client.dto;

import avia.cloud.client.entity.enums.Gender;
import lombok.*;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerDTO extends AccountBaseDTO {
    private String firstName;
    private String lastName;
    private Gender gender;
    private LocalDate dateOfBirth;
    private String nationality;
    private String passportId;
    private LocalDate passportExpiryDate;
    private byte[] image;
}
