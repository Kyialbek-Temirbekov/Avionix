package avia.cloud.discovery.repository;

import avia.cloud.discovery.dto.UserAgreementDTO;
import avia.cloud.discovery.entity.TermsOfUse;
import avia.cloud.discovery.entity.enums.Lan;
import avia.cloud.discovery.entity.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TermsOfUseRepository extends JpaRepository<TermsOfUse,String> {
    @Query("SELECT new avia.cloud.discovery.dto.UserAgreementDTO(c.title,c.description) FROM TermsOfUse t JOIN t.content c WHERE c.lan = :lan AND t.type = :type")
    List<UserAgreementDTO> findTermsOfUseBy(Lan lan, Role type);
}
