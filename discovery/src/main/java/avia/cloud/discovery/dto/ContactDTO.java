package avia.cloud.discovery.dto;

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
    private String id;
    private String name;
    private String email;
    private String message;
    private LocalDate createdAt;
}
