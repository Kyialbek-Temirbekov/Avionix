package avia.cloud.discovery.service.impl;

import avia.cloud.discovery.dto.FaqContentDTO;
import avia.cloud.discovery.dto.FaqDTO;
import avia.cloud.discovery.dto.SkylineBenefitsContentDTO;
import avia.cloud.discovery.dto.SkylineBenefitsDTO;
import avia.cloud.discovery.entity.Faq;
import avia.cloud.discovery.entity.SkylineBenefits;
import avia.cloud.discovery.repository.FaqRepository;
import avia.cloud.discovery.service.IFaqService;
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
public class FaqServiceImpl implements IFaqService {
    private final FaqRepository faqRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<FaqDTO> fetchFaq(String lan) {
        return faqRepository.findAll().stream().map(s -> convertToFaqDTO(s,lan.toUpperCase())).toList();
    }
    @SneakyThrows
    private FaqDTO convertToFaqDTO(Faq faq, String lan) {
        FaqDTO faqDTO = modelMapper.map(faq, FaqDTO.class);
        faqDTO.setContent(modelMapper.map(getField(faq.getContent(),"lan",lan), FaqContentDTO.class));
        return faqDTO;
    }
}
