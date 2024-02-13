package avia.cloud.client.service;

import avia.cloud.client.dto.Authorization;
import avia.cloud.client.dto.ClientDetails;
import avia.cloud.client.dto.VerificationInfo;

public interface IClientDetailsService {
    Authorization confirmEmail(VerificationInfo verificationInfo);
    void removeAll();
}
