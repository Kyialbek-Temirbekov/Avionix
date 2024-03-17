package avia.cloud.client.entity;

import avia.cloud.client.entity.enums.Role;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Data
@NoArgsConstructor
public class Authority {
    @Id
    @UuidGenerator
    private String id;
    @Enumerated(EnumType.STRING)
    private Role role;
    private String target;
    private boolean create;
    private boolean read;
    private boolean update;
    private boolean delete;
}
