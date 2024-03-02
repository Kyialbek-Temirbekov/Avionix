package avia.cloud.discovery.entity;

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
public class PrivacyPolicy extends BaseEntity {
    @Id
    @UuidGenerator
    private String id;
    @OneToMany(mappedBy = "privacyPolicy", fetch = FetchType.EAGER, cascade = {PERSIST,REMOVE,MERGE})
    private List<PrivacyPolicyContent> content;
}
