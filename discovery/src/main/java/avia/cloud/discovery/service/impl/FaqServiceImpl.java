package avia.cloud.discovery.service.impl;

import avia.cloud.discovery.dto.FaqDTO;
import avia.cloud.discovery.entity.enums.Lan;
import avia.cloud.discovery.repository.FaqRepository;
import avia.cloud.discovery.service.IFaqService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class FaqServiceImpl implements IFaqService {
    private final FaqRepository faqRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<FaqDTO> fetchFaq(String lan) {
        return faqRepository.findFaqsBy(Lan.of(lan));
    }

}
