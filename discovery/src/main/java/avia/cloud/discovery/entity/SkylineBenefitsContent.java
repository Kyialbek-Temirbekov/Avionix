package avia.cloud.discovery.entity;

import avia.cloud.discovery.entity.enums.Lan;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Data
@NoArgsConstructor
public class SkylineBenefitsContent {
    @Id
    @UuidGenerator
    private String id;
    @ManyToOne
    @JoinColumn(name = "skyline_benefits_id", referencedColumnName = "id")
    private SkylineBenefits skylineBenefits;
    private String title;
    private String description;
    @Enumerated(EnumType.STRING)
    private Lan lan;
}
