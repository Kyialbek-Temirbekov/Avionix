package avia.cloud.client.service;

import avia.cloud.client.dto.Authorization;
import avia.cloud.client.dto.ResponseDTO;
import avia.cloud.client.dto.VerificationInfo;
import avia.cloud.client.entity.Account;
import org.springframework.security.core.Authentication;

public interface IAccountService {
    Authorization confirmEmail(VerificationInfo verificationInfo);
    void removeAll();
    Account fetchUser(String email);
    Authorization signIn(Authentication authentication);
}
