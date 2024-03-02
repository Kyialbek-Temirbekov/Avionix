package avia.cloud.discovery.entity;

import avia.cloud.discovery.entity.enums.Lan;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Data
@NoArgsConstructor
public class TermsOfUseContent {
    @Id
    @UuidGenerator
    private String id;
    @ManyToOne
    @JoinColumn(name = "terms_of_use_id", referencedColumnName = "id")
    private TermsOfUse termsOfUse;
    private String title;
    private String description;
    @Enumerated(EnumType.STRING)
    private Lan lan;
}
