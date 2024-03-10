package avia.cloud.discovery.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;

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
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "avia.cloud.discovery.util.CustomUuidGenerator")
    private String id;
    private byte[] logo;
    @OneToMany(mappedBy = "skylineBenefits", fetch = FetchType.EAGER, cascade = {PERSIST,REMOVE,MERGE})
    private List<SkylineBenefitsContent> content;
}
