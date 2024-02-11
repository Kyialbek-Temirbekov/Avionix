package avia.cloud.client.service;

import avia.cloud.client.dto.ClientDetails;
import avia.cloud.client.dto.VerificationInfo;

public interface IClientDetailsService {
    void confirmEmail(VerificationInfo verificationInfo);
}
