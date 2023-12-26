package com.price.pegging.Repository;

import com.price.pegging.Entity.DsaExport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DsaExportRepository extends JpaRepository<DsaExport,Long> {
   @Query("select ds from DsaExport ds where ds.applicationNo=:applicationNo")
    List<DsaExport> findByApplicationNo(String applicationNo);

//    @Query("select pp from DsaExport pp where pp.applicationNo=:applicationNo AND pp.uploadDate=:updateddate")
//    List<DsaExport> findByApllicationAndUpdatedDate(String applicationNo, String updateddate);
//    @Query("select pp from DsaExport pp where pp.uploadDate=:updateddate")
//    List<DsaExport> findByUpdatedDate(String updateddate);


// @Query(value = "SELECT * FROM dsaExport p WHERE p.applicationNo = :applicationNo", nativeQuery = true)
// Page<DsaExport> findByApplicationNo(@Param("applicationNo") String applicationNo, Pageable pageable);
// @Query("select ds from DsaExport ds where ds.applicationNo=:applicationNo AND ds.uploadDate=:uploadDate")
// List<DsaExport> findByApplicationNoAndUploadDate(String applicationNo, String uploadDate);






 @Query("SELECT COUNT(ds) FROM DsaExport ds")
 Long countByfindAll();
 @Query(value = "SELECT ds FROM DsaExport ds WHERE ds.applicationNo = :applicationNo")
 Page<DsaExport> findByApplicationNo(@Param("applicationNo") String applicationNo, Pageable pageable);
 @Query("select count(ds) from DsaExport ds where ds.applicationNo = :applicationNo")
 Long countByApplicationNo(String applicationNo);
 @Query(value = "SELECT ds  FROM DsaExport ds WHERE ds.applicationNo= :applicationNo AND ds.uploadDate = :uploadDate", nativeQuery = true)
 Page<DsaExport> findByApplicationNoAndUploadDate(String applicationNo, String uploadDate, Pageable pageable);
 @Query("select count(ds) from DsaExport  ds where ds.applicationNo=:applicationNo AND ds.uploadDate=:uploadDate")
 Long countByApplicationNoAndUploadDate(String applicationNo, String uploadDate);

 @Query("select ds from DsaExport ds where ds.uploadDate=:uploadDate")
 Page<DsaExport> findByUploadDate(String uploadDate,Pageable pageable) ;
 @Query("select count(ds) from DsaExport ds where ds.uploadDate=:uploadDate")
 Long countByUploadDate(String uploadDate);
}
