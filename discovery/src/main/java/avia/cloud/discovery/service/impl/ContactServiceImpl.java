package avia.cloud.discovery.service.impl;

import avia.cloud.discovery.dto.ContactDTO;
import avia.cloud.discovery.entity.Contact;
import avia.cloud.discovery.repository.ContactRepository;
import avia.cloud.discovery.service.IContactService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class ContactServiceImpl implements IContactService {
    private final ContactRepository contactRepository;
    private final ModelMapper modelMapper;

    @Override
    public void createContact(ContactDTO contactDTO) {
        contactRepository.save(convertToContact(contactDTO));
    }

    private Contact convertToContact(ContactDTO contactDTO) {
        return modelMapper.map(contactDTO, Contact.class);
    }
}
