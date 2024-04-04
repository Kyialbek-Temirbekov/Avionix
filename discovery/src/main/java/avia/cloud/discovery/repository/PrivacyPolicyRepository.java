package avia.cloud.discovery.repository;

import avia.cloud.discovery.dto.UserAgreementDTO;
import avia.cloud.discovery.entity.PrivacyPolicy;
import avia.cloud.discovery.entity.enums.Lan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrivacyPolicyRepository extends JpaRepository<PrivacyPolicy,String> {
    @Query("SELECT new avia.cloud.discovery.dto.UserAgreementDTO(c.title,c.description) FROM PrivacyPolicy p JOIN p.content c WHERE c.lan = :lan")
    List<UserAgreementDTO> findFaqsBy(Lan lan);
}
