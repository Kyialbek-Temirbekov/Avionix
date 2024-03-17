package avia.cloud.client.util;

import avia.cloud.client.dto.AuthorityDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class AuthorityUtils {
    public static String addPrefix(String role) {
        return "ROLE_" + role;
    }
    public static List<GrantedAuthority> getAuthorities(List<AuthorityDTO> fetchedAuthorities) {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        fetchedAuthorities.forEach(fetchedAuthority -> {
            String entity = fetchedAuthority.getTarget();
            if(fetchedAuthority.isCreate()) grantedAuthorities.add(new SimpleGrantedAuthority(entity + ":create"));
            if(fetchedAuthority.isRead()) grantedAuthorities.add(new SimpleGrantedAuthority(entity + ":read"));
            if(fetchedAuthority.isUpdate()) grantedAuthorities.add(new SimpleGrantedAuthority(entity + ":update"));
            if(fetchedAuthority.isDelete()) grantedAuthorities.add(new SimpleGrantedAuthority(entity + ":delete"));

        });
        return grantedAuthorities;
    }
    public static String extractClaim(String jwt, String claim) {
        String[] jwtParts = jwt.split("\\.");

        byte[] decodedPayloadBytes = Base64.getDecoder().decode(jwtParts[1]);
        String decodedPayload = new String(decodedPayloadBytes);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree(decodedPayload);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return jsonNode.get(claim).asText();
    }
}
