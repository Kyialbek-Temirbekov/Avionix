package avia.cloud.discovery.service.impl;

import avia.cloud.discovery.dto.UserAgreementDTO;
import avia.cloud.discovery.entity.enums.Lan;
import avia.cloud.discovery.repository.PrivacyPolicyRepository;
import avia.cloud.discovery.service.IPrivacyPolicyService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PrivacyPolicyServiceImpl implements IPrivacyPolicyService {
    private final PrivacyPolicyRepository privacyPolicyRepository;

    @Override
    public List<UserAgreementDTO> fetchPrivacyPolicy(String lan) {
        return privacyPolicyRepository.findFaqsBy(Lan.of(lan));
    }
}
