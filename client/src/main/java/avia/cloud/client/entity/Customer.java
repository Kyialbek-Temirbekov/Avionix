package avia.cloud.client.entity;

import avia.cloud.client.entity.enums.Gender;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Customer {
    @Id
    private String baseId;
    private String firstName;
    private String lastName;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private LocalDate dateOfBirth;
    private String nationality;
    private String passportId;
    private LocalDate passportExpiryDate;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "base_id", referencedColumnName = "id")
    @MapsId
    private Account account;
    @OneToMany(mappedBy = "customer")
    private List<Comment> comments;
    @OneToMany(mappedBy = "customer")
    private List<AirlineRating> airlineRatings;
}
