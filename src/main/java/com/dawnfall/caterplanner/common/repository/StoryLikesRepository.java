package com.dawnfall.caterplanner.common.repository;

import com.dawnfall.caterplanner.common.entity.StoryLikes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface StoryLikesRepository extends JpaRepository<StoryLikes, StoryLikes.Key> {

    @Modifying
    @Transactional
    @Query(value = "delete from StoryLikes s where s.userId = :userId and s.storyId = :storyId")
    void deleteByPK(@Param("storyId") Long storyId, @Param("userId") Long userId);
}
