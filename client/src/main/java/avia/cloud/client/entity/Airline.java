package avia.cloud.client.entity;

import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@EqualsAndHashCode(callSuper = true)
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class Airline extends AccountBase {
    /**
     * The IATA code is a three-letter code assigned by the International Air Transport Association
     * (IATA) to identify airports, airlines, and other air transport entities.
     */
    private String IATA;
    private String name;
    private byte[] logo;
    private String address;
    private String officialWebsiteUrl;
    private String description;
    private String clientId;
    private String clientSecret;
    private int priority;
}
