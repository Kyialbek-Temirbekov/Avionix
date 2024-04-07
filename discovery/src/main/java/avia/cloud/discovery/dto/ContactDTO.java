package avia.cloud.discovery.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContactDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String id;
    @NotNull
    @NotBlank
    @Size(max = 50)
    private String name;
    @NotNull
    @NotBlank
    @Size(max = 50)
    @Email
    private String email;
    @NotNull
    @NotBlank
    private String message;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDate createdAt;
}
