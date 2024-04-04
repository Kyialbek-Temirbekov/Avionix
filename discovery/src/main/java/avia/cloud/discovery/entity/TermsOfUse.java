package avia.cloud.discovery.entity;

import avia.cloud.discovery.entity.enums.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
public class TermsOfUse extends BaseEntity {
    @Id
    @UuidGenerator
    private String id;
    private Role type;
    @OneToMany(mappedBy = "termsOfUse", fetch = FetchType.EAGER, cascade = {PERSIST,REMOVE,MERGE})
    private List<TermsOfUseContent> content;
}
