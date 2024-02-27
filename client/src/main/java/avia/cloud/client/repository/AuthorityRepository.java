package avia.cloud.client.repository;

import avia.cloud.client.entity.Authority;
import avia.cloud.client.entity.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority,String> {
    List<Authority> findAllByRoleIn(List<Role> role);
}
