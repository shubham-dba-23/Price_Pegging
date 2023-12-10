package com.price.pegging.Repository;

import com.price.pegging.Entity.PricePeggingUpload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PricePeggingRepository extends JpaRepository<PricePeggingUpload,Long> {
}
