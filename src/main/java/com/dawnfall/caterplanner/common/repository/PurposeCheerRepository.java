package com.dawnfall.caterplanner.common.repository;

import com.dawnfall.caterplanner.common.entity.PurposeCheer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface PurposeCheerRepository extends JpaRepository<PurposeCheer, PurposeCheer.Key> {

    @Modifying
    @Transactional
    @Query(value = "delete from PurposeCheer p where p.userId = :userId and p.purposeId = :purposeId")
    void deleteByPK(@Param("purposeId") Long purposeId, @Param("userId") Long userId);

}
