package avia.cloud.gatewayserver.dto;

import org.springframework.http.HttpMethod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RequestBuilder {
    private static final List<RequestDTO> requests = new ArrayList<>();
    public RequestBuilder pathMatchers(HttpMethod httpMethod, String... paths) {
        Arrays.stream(paths).forEach(p -> requests.add(new RequestDTO(httpMethod,p)));
        return this;
    }
    public List<RequestDTO> build() {
        return requests;
    }
}
