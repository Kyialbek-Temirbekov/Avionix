package avia.cloud.client.entity;

import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.CascadeType.REMOVE;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Airline {
    /**
     * The IATA code is a three-letter code assigned by the International Air Transport Association
     * (IATA) to identify airports, airlines, and other air transport entities.
     */
    @Id
    private String baseId;
    private String iata;
    private String name;
    @Embedded
    private Address address;
    private String officialWebsiteUrl;
    private String description;
    private String clientId;
    private String clientSecret;
    private int priority;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "base_id", referencedColumnName = "id")
    @MapsId
    private Account account;
}
