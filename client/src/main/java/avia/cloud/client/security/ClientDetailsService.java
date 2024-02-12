package avia.cloud.client.security;

import avia.cloud.client.entity.AccountBase;
import avia.cloud.client.repository.AirlineRepository;
import avia.cloud.client.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ClientDetailsService implements UserDetailsService {
    private final CustomerRepository customerRepository;
    private final AirlineRepository airlineRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        List<GrantedAuthority> authorities = new ArrayList<>();
        AccountBase account = airlineRepository.findByEmailAndEnabledTrue(email).map(airline -> (AccountBase)airline)
                .or(() -> customerRepository.findByEmailAndEnabledTrue(email).map(customer -> (AccountBase)customer))
                .orElseThrow(() -> new UsernameNotFoundException("User details not found for user: " + email));
        account.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.toString())));
        return new User(account.getEmail(), account.getPassword(), account.isEnabled(), true, true, account.isNonLocked(), authorities);
    }
}
