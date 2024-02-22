package avia.cloud.discovery.repository;

import avia.cloud.discovery.entity.Faq;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FaqRepository extends JpaRepository<Faq,String> {
}
