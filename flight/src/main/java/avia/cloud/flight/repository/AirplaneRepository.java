package avia.cloud.flight.repository;

import avia.cloud.flight.entity.Airplane;
import avia.cloud.flight.entity.enums.Cabin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AirplaneRepository extends JpaRepository<Airplane,String> {
    Airplane findFirstByCabinsCabin(Cabin cabin);
}
