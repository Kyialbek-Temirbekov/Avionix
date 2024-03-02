package avia.cloud.discovery.entity;

import avia.cloud.discovery.entity.enums.Lan;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Data
@NoArgsConstructor
public class PrivacyPolicyContent {
    @Id
    @UuidGenerator
    private String id;
    @ManyToOne
    @JoinColumn(name = "privacy_policy_id", referencedColumnName = "id")
    private PrivacyPolicy privacyPolicy;
    private String title;
    private String description;
    @Enumerated(EnumType.STRING)
    private Lan lan;
}
