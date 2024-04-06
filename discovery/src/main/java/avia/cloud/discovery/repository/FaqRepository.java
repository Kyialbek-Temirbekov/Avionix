package avia.cloud.discovery.repository;

import avia.cloud.discovery.dto.FaqDTO;
import avia.cloud.discovery.entity.Faq;
import avia.cloud.discovery.entity.enums.Lan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FaqRepository extends JpaRepository<Faq,String> {
    @Query("SELECT new avia.cloud.discovery.dto.FaqDTO(c.question,c.answer) FROM Faq f JOIN f.content c WHERE c.lan = :lan")
    List<FaqDTO> findFaqsBy(Lan lan);
    @Query("SELECT f FROM Faq f JOIN f.content c WHERE LOWER(c.question) LIKE LOWER(CONCAT('%',:text,'%')) OR LOWER(c.answer) LIKE LOWER(CONCAT('%',:text,'%'))")
    List<Faq> findFaqsByText(String text);
}
