package avia.cloud.discovery.repository;

import avia.cloud.discovery.entity.SkylineBenefits;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SkylineBenefitsRepository extends JpaRepository<SkylineBenefits,String> {
    SkylineBenefits findByCreatedBy(String name);
}
