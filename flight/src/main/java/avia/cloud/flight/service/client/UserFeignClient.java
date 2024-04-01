package avia.cloud.flight.service.client;

import avia.cloud.flight.dto.AuthorityDTO;
import feign.Headers;
import feign.Param;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("client")
public interface UserFeignClient {
    @GetMapping("/api/authority")
    List<AuthorityDTO> fetchAuthorities(@RequestParam String authorities);
    @GetMapping("/api/customer/id")
    ResponseEntity<String> findCustomerId(@RequestHeader("Authorization") String token);
}
