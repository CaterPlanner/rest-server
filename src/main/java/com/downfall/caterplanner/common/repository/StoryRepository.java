package com.downfall.caterplanner.common.repository;

import com.downfall.caterplanner.common.entity.Story;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoryRepository extends JpaRepository<Story, Long> {

    public List<Story> findTop10ByPurposeId(Long purposeId);

    public List<Story> findByPurposeId(Long purposeId);

}
