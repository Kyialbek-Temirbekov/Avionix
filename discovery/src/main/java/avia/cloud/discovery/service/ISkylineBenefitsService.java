package avia.cloud.discovery.service;

import avia.cloud.discovery.dto.SkylineBenefitsDTO;
import avia.cloud.discovery.entity.SkylineBenefits;

import java.util.List;

public interface ISkylineBenefitsService {
    List<SkylineBenefitsDTO> fetchSkylineBenefits(String lan);
}
