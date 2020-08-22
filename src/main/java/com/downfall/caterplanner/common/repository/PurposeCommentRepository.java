package com.downfall.caterplanner.common.repository;

import com.downfall.caterplanner.common.entity.PurposeComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurposeCommentRepository extends JpaRepository<PurposeComment, Long> {
}