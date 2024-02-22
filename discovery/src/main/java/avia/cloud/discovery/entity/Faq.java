package avia.cloud.discovery.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.*;
import lombok.experimental.SuperBuilder;

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
public class Faq extends BaseEntity{
    @Id
    private String id;
    @OneToMany(mappedBy = "faq", fetch = FetchType.EAGER, cascade = {PERSIST,REMOVE,MERGE})
    private List<FaqContent> content;
}
