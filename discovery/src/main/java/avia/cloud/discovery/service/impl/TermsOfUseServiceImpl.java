package avia.cloud.discovery.service.impl;

import avia.cloud.discovery.dto.UserAgreementDTO;
import avia.cloud.discovery.entity.enums.Lan;
import avia.cloud.discovery.entity.enums.Role;
import avia.cloud.discovery.repository.TermsOfUseRepository;
import avia.cloud.discovery.service.ITermsOfUseService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TermsOfUseServiceImpl implements ITermsOfUseService {
    private final TermsOfUseRepository termsOfUseRepository;

    @Override
    public List<UserAgreementDTO> fetchTermsOfUse(String lan, Role type) {
        return termsOfUseRepository.findTermsOfUseBy(Lan.of(lan), type);
    }
}
