package avia.cloud.flight.dto;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data @AllArgsConstructor
public class ResponseDTO {
    private String responseCode;
    private String responseMsg;
}
