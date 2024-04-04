package avia.cloud.discovery.service;

import avia.cloud.discovery.dto.SkylineBenefitsDTO;

import java.util.List;

public interface ISkylineBenefitsService {
    List<SkylineBenefitsDTO> fetchSkylineBenefits(String lan);
}
