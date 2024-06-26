package avia.cloud.client.dto;

import avia.cloud.client.entity.enums.Lan;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String id;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String author;
    @NotNull
    @NotBlank
    private String description;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDate createdAt;
    @NotNull
    @Max(5)
    @PositiveOrZero
    private int grade;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private boolean checked;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull
    private Lan lan;
}
