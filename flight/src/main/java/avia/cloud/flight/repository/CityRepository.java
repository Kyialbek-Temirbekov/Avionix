package avia.cloud.flight.repository;

import avia.cloud.flight.dto.CityDTO;
import avia.cloud.flight.entity.City;
import avia.cloud.flight.entity.enums.Lan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CityRepository extends JpaRepository<City,String> {
    @Query("SELECT i.name FROM City c JOIN c.names i WHERE c.code = :code AND i.lan = :lan")
    String findByCodeAndLan(String code, Lan lan);
    @Query("SELECT new avia.cloud.flight.dto.CityDTO(c.code, i.name) FROM City c JOIN c.names i WHERE i.lan = :lan")
    List<CityDTO> findByLan(Lan lan);
}
