package com.downfall.caterplanner.common.repository;

import com.downfall.caterplanner.common.entity.Purpose;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurposeRepository extends JpaRepository<Purpose, Long> {

    Page<Purpose> findByNameStartsWith(String prefix, Pageable pageable);

}
