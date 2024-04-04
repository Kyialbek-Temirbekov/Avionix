package avia.cloud.discovery.service;

import avia.cloud.discovery.dto.UserAgreementDTO;
import avia.cloud.discovery.entity.enums.Role;

import java.util.List;

public interface ITermsOfUseService {
    List<UserAgreementDTO> fetchTermsOfUse(String lan, Role type);
}
