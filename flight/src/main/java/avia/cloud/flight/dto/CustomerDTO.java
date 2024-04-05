package avia.cloud.flight.dto;

import avia.cloud.flight.entity.enums.Gender;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDTO {
    @NotNull
    @NotBlank
    @Size(max = 50)
    private String firstName;
    @NotNull
    @NotBlank
    @Size(max = 50)
    private String lastName;
    @NotNull
    private Gender gender;
    @Past
    private LocalDate dateOfBirth;
    @NotNull
    @NotBlank
    @Size(max = 50)
    private String nationality;
    @NotNull
    @NotBlank
    @Size(max = 50)
    @Pattern(regexp="[A-Za-z0-9]*", message="passport ID must contain only digits and letters")
    private String passportId;
    @Future
    private LocalDate passportExpiryDate;
    @JsonUnwrapped
    @Valid
    private AccountDTO account;
}
