package avia.cloud.flight.repository;

import avia.cloud.flight.entity.SpecialDeal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpecialDealRepository extends JpaRepository<SpecialDeal,String> {
}
