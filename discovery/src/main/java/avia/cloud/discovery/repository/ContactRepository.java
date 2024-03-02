package avia.cloud.discovery.repository;

import avia.cloud.discovery.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRepository extends JpaRepository<Contact,String> {
}
