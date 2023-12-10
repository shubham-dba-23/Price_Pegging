package com.price.pegging.Repository;

import com.price.pegging.Entity.DsaExport;
import com.price.pegging.Entity.PricePegging;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PricePeggingRepository extends JpaRepository<PricePegging,Long> {

   @Query("select pp from PricePegging pp where pp.zone=:zone")
    List<PricePegging> findByZone(String zone);
}
