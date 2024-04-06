package avia.cloud.flight.repository;

import avia.cloud.flight.entity.Article;
import avia.cloud.flight.entity.SpecialDeal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpecialDealRepository extends JpaRepository<SpecialDeal,String> {
    @Query("SELECT a FROM Article a JOIN a.content c WHERE  LOWER(c.description) LIKE LOWER(CONCAT('%',:text,'%'))")
    List<Article> findSpecialDealsByText(String text);
}
