package avia.cloud.client.security;

import avia.cloud.client.entity.Account;
import avia.cloud.client.service.IAccountService;
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
    private final IAccountService iUserService;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        List<GrantedAuthority> authorities = new ArrayList<>();
        Account account = iUserService.fetchUser(email);
        account.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.toString())));
        return new User(account.getEmail(), account.getPassword(), account.isEnabled(), true, true, account.isNonLocked(), authorities);
    }
}
