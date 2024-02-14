package avia.cloud.gatewayserver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpMethod;

@Data
@AllArgsConstructor
public class RequestDTO {
    HttpMethod httpMethod;
    String path;
    public static RequestBuilder builder() {
        return new RequestBuilder();
    }
}
