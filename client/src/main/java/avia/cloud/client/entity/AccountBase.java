package avia.cloud.client.entity;

import avia.cloud.client.entity.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.UuidGenerator;

import java.util.List;

@MappedSuperclass
@EqualsAndHashCode(callSuper = true)
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class AccountBase extends BaseEntity {
    @Id
    @UuidGenerator
    private String id;
    private String email;
    private String phone;
    private String password;
    @Enumerated(EnumType.STRING)
    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    private List<Role> roles;
    private boolean enabled;
    private boolean nonLocked;
    private boolean agreedToPrivacyPolicy;
    private String code;
}
