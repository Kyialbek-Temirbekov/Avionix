package avia.cloud.client.repository;

import avia.cloud.client.entity.Airline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AirlineRepository extends JpaRepository<Airline,String> {

    Optional<Airline> findByEmail(String username);
    Optional<Airline> findByEmailAndEnabledTrue(String username);
    Optional<Airline> findByClientId(String clientId);
}
