package avia.cloud.discovery.service.client;

import avia.cloud.discovery.dto.AuthorityDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("client")
public interface UserFeignClient {
    @GetMapping("/api/authority")
    public List<AuthorityDTO> fetchAuthorities(@RequestParam String authorities);
}
