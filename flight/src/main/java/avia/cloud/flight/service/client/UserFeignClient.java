package avia.cloud.flight.service.client;

import avia.cloud.flight.dto.AirlineDTO;
import avia.cloud.flight.dto.AuthorityDTO;
import avia.cloud.flight.dto.CommentDTO;
import avia.cloud.flight.dto.CustomerDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("client")
public interface UserFeignClient {
    @GetMapping("/api/authority")
    List<AuthorityDTO> fetchAuthorities(@RequestParam String authorities);
    @GetMapping("/api/customer/{customerId}")
    ResponseEntity<CustomerDTO> fetchCustomer(@PathVariable String customerId, @RequestHeader("Authorization") String token);
    @GetMapping("/api/account/id")
    ResponseEntity<String> findAccountId(@RequestHeader("Authorization") String token);
    @GetMapping("/api/airline/{airlineId}")
    ResponseEntity<AirlineDTO> findAirline(@PathVariable String airlineId);
    @GetMapping("/api/airline/ids")
    ResponseEntity<List<String>> findAirlineIds(@RequestParam String text);
    @GetMapping("/api/comment/global")
    ResponseEntity<List<CommentDTO>> fetchCommentsByText(@RequestParam String text);
    @GetMapping("/api/airline")
    ResponseEntity<AirlineDTO> fetchCustomer(@RequestHeader("Authorization") String token);
}
