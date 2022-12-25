package com.jobsnapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jobsnapp.model.InterestReaction;
import com.jobsnapp.model.Job;

public interface JobsRepository extends JpaRepository<Job, Long> {
}
