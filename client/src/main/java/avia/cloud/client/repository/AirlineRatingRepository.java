package avia.cloud.client.repository;

import avia.cloud.client.entity.AirlineRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AirlineRatingRepository extends JpaRepository<AirlineRating,String> {
    @Query("SELECT new avia.cloud.client.entity.AirlineRating(a.airline, CAST(SUM(a.grade) AS short) ) FROM AirlineRating a GROUP BY a.airline")
    List<AirlineRating> findAirlineRatingBy();
}
