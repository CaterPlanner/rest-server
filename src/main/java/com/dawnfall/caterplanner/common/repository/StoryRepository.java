package com.dawnfall.caterplanner.common.repository;

import com.dawnfall.caterplanner.common.entity.Story;
import com.dawnfall.caterplanner.common.entity.enumerate.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoryRepository extends JpaRepository<Story, Long> {

    List<Story> findTop10ByPurposeId(Long purposeId);

    Page<Story> findByPurposeId(Long purposeId, Pageable pageable);

    Page<Story> findAllByType(Integer type, Pageable pageable);

    Page<Story> findByPurposeIdAndDisclosureScope(Long id, Scope disclosureScope, Pageable pageable);

    Page<Story> findAllByDisclosureScope(Scope disclosureScope, Pageable pageable);
}
