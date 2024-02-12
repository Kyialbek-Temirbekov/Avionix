package avia.cloud.client.repository;

import avia.cloud.client.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,String> {
    Optional<Customer> findByEmail(String username);
    Optional<Customer> findByEmailAndEnabledTrue(String username);
}
