package avia.cloud.flight.repository;

import avia.cloud.flight.dto.ArticleDTO;
import avia.cloud.flight.entity.Article;
import avia.cloud.flight.entity.enums.Lan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article,String> {
}
