package avia.cloud.client.entity;

import avia.cloud.client.entity.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.UuidGenerator;

import java.util.List;

import static jakarta.persistence.CascadeType.MERGE;
import static jakarta.persistence.CascadeType.REMOVE;

@Entity
@EqualsAndHashCode(callSuper = true)
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class Account extends BaseEntity {
    @Id
    @UuidGenerator
    private String id;
    private String email;
    private String phone;
    private byte[] image;
    private String password;
    @Enumerated(EnumType.STRING)
    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    private List<Role> roles;
    private boolean enabled;
    private boolean nonLocked;
    private boolean agreedToTermsOfUse;
    private String code;
    @OneToOne(mappedBy = "account", cascade = {REMOVE})
    private Airline airline;
    @OneToOne(mappedBy = "account", cascade = {REMOVE})
    private Customer customer;
}
