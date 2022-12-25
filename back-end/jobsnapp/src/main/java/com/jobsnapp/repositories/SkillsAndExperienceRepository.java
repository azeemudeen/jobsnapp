package com.jobsnapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jobsnapp.model.SkillsAndExperience;
import com.jobsnapp.model.User;

public interface SkillsAndExperienceRepository extends JpaRepository<SkillsAndExperience, Long> {
}
