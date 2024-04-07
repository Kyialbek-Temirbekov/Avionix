package avia.cloud.discovery.service;

import avia.cloud.discovery.dto.ContactDTO;
import avia.cloud.discovery.entity.Contact;

import java.util.List;

public interface IContactService {
    void createContact(ContactDTO contactDTO);
    List<Contact> getContacts();
}
