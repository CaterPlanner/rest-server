package com.dawnfall.caterplanner.common.repository;

import com.dawnfall.caterplanner.common.entity.PurposeComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurposeCommentRepository extends JpaRepository<PurposeComment, Long> {
    Page<PurposeComment> findByPurposeId(Long purposeId, Pageable pageable);
}
