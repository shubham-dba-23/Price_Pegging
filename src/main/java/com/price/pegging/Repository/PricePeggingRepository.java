package com.price.pegging.Repository;

import com.price.pegging.Entity.PricePegging;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PricePeggingRepository extends JpaRepository<PricePegging,Long> {

//   @Query("select pp from PricePegging pp where pp.zone=:zone")
//    List<PricePegging> findByZone(String zone);
//    @Query("select DISTINCT(pp.zone) pp from PricePegging pp")
//    List getUniqeZones();



    @Query(value = "SELECT * FROM PricePegging p WHERE p.zone = :zone", nativeQuery = true)
    Page<PricePegging> findByZone(@Param("zone") String zone, Pageable pageable);
    @Query(value = "SELECT * FROM PricePegging p WHERE p.zone = :zone AND p.updatedDate = :updatedDate", nativeQuery = true)
    Page<PricePegging> findByZoneAndUploadDate(@Param("zone") String zone, @Param("updatedDate") String updatedDate, Pageable pageable);

    @Query(value = "SELECT * FROM PricePegging p WHERE p.uploadDate = :uploadDate", nativeQuery = true)
    Page<PricePegging> findByUploadDate(@Param("uploadDate") String uploadDate, Pageable pageable);


//    @Query("select pp from PricePegging pp where pp.uploadDate=:uploadDate")
//    List<PricePegging> findByUploadDate(String uploadDate);
 @Query("select count(p) from PricePegging p where p.zone=:zone AND p.uploadDate=:uploadDate")
    Long countByZoneAndUploadDate(String zone, String uploadDate);
 @Query("select count(pp) from PricePegging pp where pp.zone = :zone")
 Long countByZone(String zone);
 @Query("select count(pp) from PricePegging pp where pp.uploadDate = :uploadDate")
 Long countByUploadDate(String uploadDate);
 @Query("SELECT COUNT(pp) FROM PricePegging pp")
 Long countByfindAll();

//    List getUniqeZones();


}
