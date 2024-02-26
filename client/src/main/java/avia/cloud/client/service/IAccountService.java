package avia.cloud.client.service;

import avia.cloud.client.dto.Authorization;
import avia.cloud.client.dto.VerificationInfo;
import avia.cloud.client.entity.Account;

public interface IAccountService {
    Authorization confirmEmail(VerificationInfo verificationInfo);
    void removeAll();
    Account fetchUser(String email);
}
