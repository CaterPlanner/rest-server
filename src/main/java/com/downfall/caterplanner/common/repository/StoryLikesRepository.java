package com.downfall.caterplanner.common.repository;

import com.downfall.caterplanner.common.entity.StoryLikes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoryLikesRepository extends JpaRepository<StoryLikes, StoryLikes.Key> {
}
