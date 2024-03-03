package avia.cloud.discovery.util;

import avia.cloud.discovery.dto.AuthorityDTO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;

public class AuthorityUtils {
    public static String addPrefix(String role) {
        return "ROLE_" + role;
    }
    public static List<GrantedAuthority> getAuthorities(List<AuthorityDTO> fetchedAuthorities) {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        fetchedAuthorities.forEach(fetchedAuthority -> {
            String entity = fetchedAuthority.getEntity();
            if(fetchedAuthority.isCreate()) grantedAuthorities.add(new SimpleGrantedAuthority(entity + ":create"));
            if(fetchedAuthority.isRead()) grantedAuthorities.add(new SimpleGrantedAuthority(entity + ":read"));
            if(fetchedAuthority.isUpdate()) grantedAuthorities.add(new SimpleGrantedAuthority(entity + ":update"));
            if(fetchedAuthority.isDelete()) grantedAuthorities.add(new SimpleGrantedAuthority(entity + ":delete"));

        });
        return grantedAuthorities;
    }
}
