package avia.cloud.client.repository;

import avia.cloud.client.entity.Comment;
import avia.cloud.client.entity.enums.Lan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, String> {
    List<Comment> findAllByLanAndCheckedTrue(Lan lan);
    @Query("SELECT c FROM Comment c WHERE c.checked = true AND LOWER(c.description) LIKE LOWER(concat('%',:text,'%') ) ")
    List<Comment> findAllByText(String text);
}
