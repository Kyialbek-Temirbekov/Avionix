package avia.cloud.discovery.entity;

import avia.cloud.discovery.entity.enums.Lan;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
public class FaqContent {
    @Id
    private String id;
    @ManyToOne
    @JoinColumn(name = "faq_id", referencedColumnName = "id")
    private Faq faq;
    private String question;
    private String answer;
    @Enumerated(EnumType.STRING)
    private Lan lan;
}
