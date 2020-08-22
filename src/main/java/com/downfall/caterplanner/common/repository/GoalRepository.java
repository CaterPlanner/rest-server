package com.downfall.caterplanner.common.repository;

import com.downfall.caterplanner.common.entity.Goal;
import com.downfall.caterplanner.common.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GoalRepository extends JpaRepository<Goal, Goal.Key> {
}
