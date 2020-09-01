package com.downfall.caterplanner.common.repository;

import com.downfall.caterplanner.common.entity.PurposeComment;
import com.downfall.caterplanner.common.entity.StoryComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoryCommentRepository extends JpaRepository<StoryComment, Long> {

    public Page<StoryComment> findByStoryId(Long storyId, Pageable pageable);

    public List<StoryComment> findTop10ByStoryId(Long storyId);


}
