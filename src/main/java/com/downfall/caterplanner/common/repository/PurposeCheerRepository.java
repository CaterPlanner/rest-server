package com.downfall.caterplanner.common.repository;

import com.downfall.caterplanner.common.entity.PurposeCheer;
import com.downfall.caterplanner.common.entity.PurposeComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurposeCheerRepository extends JpaRepository<PurposeCheer, PurposeCheer.Key> {
}
