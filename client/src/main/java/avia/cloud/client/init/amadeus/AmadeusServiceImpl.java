package avia.cloud.client.init.amadeus;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class AmadeusServiceImpl extends RestTemplate implements IAmadeusService {
    @Value("${application.amadeus.client.key}")
    public String clientId;
    @Value("${application.amadeus.client.secret}")
    public String clientSecret;

    @Value("${application.amadeus.apis.accessToken}")
    public String accessTokenApi;
    @Value("${application.amadeus.apis.flightOffersSearch}")
    public String flightOffersSearchApi;
    @Value("${application.amadeus.apis.airportSearch}")
    public String airportSearchApi;

    public void sendTokenRequest() {
        String url = "https://test.api.amadeus.com/v1/security/oauth2/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "client_credentials");
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<String> responseEntity = postForEntity(url, requestEntity, String.class);

        HttpStatusCode statusCode = responseEntity.getStatusCode();
        String responseBody = responseEntity.getBody();
        System.out.println(responseBody);
    }

}
