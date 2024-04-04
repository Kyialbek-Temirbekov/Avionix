package avia.cloud.discovery.service;

import avia.cloud.discovery.dto.UserAgreementDTO;

import java.util.List;

public interface IPrivacyPolicyService {
    List<UserAgreementDTO> fetchPrivacyPolicy(String lan);
}
