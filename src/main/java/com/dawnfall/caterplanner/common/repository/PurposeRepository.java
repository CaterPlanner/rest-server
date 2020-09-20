package com.dawnfall.caterplanner.common.repository;

import com.dawnfall.caterplanner.common.entity.enumerate.Stat;
import com.dawnfall.caterplanner.common.entity.Purpose;
import com.dawnfall.caterplanner.common.entity.enumerate.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurposeRepository extends JpaRepository<Purpose, Long> {

    Page<Purpose> findByNameStartsWith(String prefix, Pageable pageable);


    Page<Purpose> findByDisclosureScopeAndNameStartsWith(Scope disclosureScope, String name, Pageable pageable);

    List<Purpose> findByUserIdAndStatIn(Long id, @Param("stat") List<Stat> stat);
}
