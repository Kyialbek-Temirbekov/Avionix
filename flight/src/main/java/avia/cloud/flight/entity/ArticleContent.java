package avia.cloud.flight.entity;

import avia.cloud.flight.entity.enums.Lan;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Data
@NoArgsConstructor
public class ArticleContent {
    @Id
    @UuidGenerator
    private String id;
    @ManyToOne
    @JoinColumn(name = "article_id", referencedColumnName = "id")
    private Article article;
    private String description;
    @Enumerated(EnumType.STRING)
    private Lan lan;
}
