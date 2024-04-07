package avia.cloud.discovery.controller;

import avia.cloud.discovery.dto.ContactDTO;
import avia.cloud.discovery.service.IContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contact")
@RequiredArgsConstructor
@Validated
public class ContactController {
    private final IContactService iContactService;
    @PostMapping()
    public ResponseEntity<?> createContact(@RequestBody ContactDTO contactDTO) {
        iContactService.createContact(contactDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }
    @GetMapping()
    public ResponseEntity<?> getAllContacts() {
        return ResponseEntity.status(HttpStatus.OK).body(iContactService.getContacts());
    }
}
