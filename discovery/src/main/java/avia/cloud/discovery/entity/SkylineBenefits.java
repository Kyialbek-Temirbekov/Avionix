package avia.cloud.discovery.entity;

import jakarta.persistence.*;
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
public class SkylineBenefits extends BaseEntity {
    @Id
    private String id;
    private byte[] logo;
    @OneToMany(mappedBy = "skylineBenefits", fetch = FetchType.EAGER, cascade = {PERSIST,REMOVE,MERGE})
    private List<SkylineBenefitsContent> content;
}
