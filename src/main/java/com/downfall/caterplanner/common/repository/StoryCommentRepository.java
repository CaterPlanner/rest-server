package com.downfall.caterplanner.common.repository;

import com.downfall.caterplanner.common.entity.PurposeComment;
import com.downfall.caterplanner.common.entity.StoryComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoryCommentRepository extends JpaRepository<StoryComment, Long> {

    Page<StoryComment> findByStoryId(Long storyId, Pageable pageable);

}
