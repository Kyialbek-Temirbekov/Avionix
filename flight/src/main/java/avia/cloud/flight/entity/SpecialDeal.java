package avia.cloud.flight.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;
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
public class SpecialDeal extends BaseEntity {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "avia.cloud.flight.util.CustomUuidGenerator")
    private String id;
    private String iata;
    private byte[] image;
    @OneToMany(mappedBy = "specialDeal", fetch = FetchType.EAGER, cascade = {PERSIST,REMOVE,MERGE})
    private List<SpecialDealContent> content;
    private String cityCode;
}
