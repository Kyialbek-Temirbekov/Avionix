package avia.cloud.client.service;

import avia.cloud.client.dto.Authorization;
import avia.cloud.client.dto.PasswordResetInfo;
import avia.cloud.client.dto.ResponseDTO;
import avia.cloud.client.dto.VerificationInfo;
import avia.cloud.client.dto.management.AccountMDTO;
import avia.cloud.client.entity.Account;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface IAccountService {
    Authorization confirmEmail(VerificationInfo verificationInfo);
    void removeAll();
    Account fetchUser(String email);
    Authorization signIn(Authentication authentication);
    void forgotPassword(String email);
    void resetPassword(PasswordResetInfo passwordResetInfo);
    String findAccountId(String email);
    List<AccountMDTO> fetchAllAccounts();
    void lockAccount(String id);
    void unlockAccount(String id);
}
