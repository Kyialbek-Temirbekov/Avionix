package avia.cloud.discovery.service.impl;

import avia.cloud.discovery.dto.SkylineBenefitsContentDTO;
import avia.cloud.discovery.dto.SkylineBenefitsDTO;
import avia.cloud.discovery.entity.SkylineBenefits;
import avia.cloud.discovery.entity.enums.Lan;
import avia.cloud.discovery.repository.SkylineBenefitsRepository;
import avia.cloud.discovery.service.ISkylineBenefitsService;
import avia.cloud.discovery.util.ImageUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

import static avia.cloud.discovery.util.FieldUtils.getField;


@Service
@Transactional
@RequiredArgsConstructor
public class SkylineBenefitsServiceImpl implements ISkylineBenefitsService {
    private final SkylineBenefitsRepository skylineBenefitsRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<SkylineBenefitsDTO> fetchSkylineBenefits(String lan) {
        return skylineBenefitsRepository.findAll().stream().map(skylineBenefits -> convertToSkylineBenefitsDTO(skylineBenefits, Lan.of(lan))).toList();
    }
    @SneakyThrows
    private SkylineBenefitsDTO convertToSkylineBenefitsDTO(SkylineBenefits skylineBenefits, Lan lan) {
        SkylineBenefitsDTO skylineBenefitsDTO = modelMapper.map(skylineBenefits, SkylineBenefitsDTO.class);
        skylineBenefitsDTO.setContent(modelMapper.map(getField(skylineBenefits.getContent(), lan),SkylineBenefitsContentDTO.class));
        if (skylineBenefits.getLogo() != null) {
            skylineBenefitsDTO.setLogoUrl(ImageUtils.getBase64Image(skylineBenefits.getLogo()));
        }
        return skylineBenefitsDTO;
    }
}
