package avia.cloud.discovery.repository;

import avia.cloud.discovery.dto.FaqDTO;
import avia.cloud.discovery.dto.SkylineBenefitsDTO;
import avia.cloud.discovery.entity.SkylineBenefits;
import avia.cloud.discovery.entity.enums.Lan;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkylineBenefitsRepository extends JpaRepository<SkylineBenefits,String> {
    @Query("SELECT s FROM SkylineBenefits s JOIN s.content c WHERE (LOWER(c.title) LIKE LOWER(CONCAT('%',:text,'%')) OR LOWER(c.description) LIKE LOWER(CONCAT('%',:text,'%')))")
    List<SkylineBenefits> findSkylineBenefitsByText(String text);
}
