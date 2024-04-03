package avia.cloud.flight.entity;

import avia.cloud.flight.entity.enums.Lan;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SpecialDealContent {
    @Id
    @UuidGenerator
    private String id;
    @ManyToOne
    @JoinColumn(name = "special_deal_id", referencedColumnName = "id")
    private SpecialDeal specialDeal;
    private String description;
    @Enumerated(EnumType.STRING)
    private Lan lan;
}
