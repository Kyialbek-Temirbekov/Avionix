package avia.cloud.discovery.service.impl;

import avia.cloud.discovery.dto.SkylineBenefitsDTO;
import avia.cloud.discovery.entity.SkylineBenefits;
import avia.cloud.discovery.entity.SkylineBenefitsContent;
import avia.cloud.discovery.entity.enums.Lan;
import avia.cloud.discovery.repository.SkylineBenefitsRepository;
import avia.cloud.discovery.service.ISkylineBenefitsService;
import avia.cloud.discovery.util.ImageUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class SkylineBenefitsServiceImpl implements ISkylineBenefitsService {
    private final SkylineBenefitsRepository skylineBenefitsRepository;

    @Override
    public List<SkylineBenefitsDTO> fetchSkylineBenefits(String lan) {
        return skylineBenefitsRepository.findAll().stream().map(skylineBenefits -> convertToSkylineBenefitsDTO(skylineBenefits, Lan.of(lan))).toList();
    }
    @SneakyThrows
    private SkylineBenefitsDTO convertToSkylineBenefitsDTO(SkylineBenefits skylineBenefits, Lan lan) {
        SkylineBenefitsContent skylineBenefitsContent = skylineBenefits.getContent().stream().filter(content -> content.getLan().equals(lan)).findFirst().get();
        String imageUrl = null;
        if (skylineBenefits.getLogo() != null) {
            imageUrl = ImageUtils.getBase64Image(skylineBenefits.getLogo());
        }
        return SkylineBenefitsDTO.builder()
                .title(skylineBenefitsContent.getTitle())
                .description(skylineBenefitsContent.getDescription())
                .logoUrl(imageUrl).build();
    }
}
