package avia.cloud.flight.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.UuidGenerator;

import java.util.List;

import static jakarta.persistence.CascadeType.*;

@Entity
@EqualsAndHashCode(callSuper = true)
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class Article extends BaseEntity {
    @Id
    @UuidGenerator
    private String id;
    private String iata;
    private byte[] image;
    @OneToMany(mappedBy = "article", fetch = FetchType.EAGER, cascade = {PERSIST,REMOVE,MERGE})
    private List<ArticleContent> content;
    @OneToOne(mappedBy = "article", fetch = FetchType.EAGER)
    private Flight flight;
    @Transient
    private String cityCode;
}
