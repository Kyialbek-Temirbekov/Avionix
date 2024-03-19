package avia.cloud.flight.service.client;

import avia.cloud.flight.dto.AuthorityDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("client")
public interface AuthorityFeignClient {
    @GetMapping("/api/authority")
    public List<AuthorityDTO> fetchAuthorities(@RequestParam String authorities);
}
