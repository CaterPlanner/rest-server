package com.dawnfall.caterplanner.common.repository;

import com.dawnfall.caterplanner.common.entity.Goal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GoalRepository extends JpaRepository<Goal, Goal.Key> {
}
