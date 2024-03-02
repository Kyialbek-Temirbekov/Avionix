package avia.cloud.discovery.repository;

import avia.cloud.discovery.entity.TermsOfUse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TermsOfUseRepository extends JpaRepository<TermsOfUse,String> {
}
