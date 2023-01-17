package com.jobsnapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jobsnapp.model.Resume;

public interface ResumeRepository extends JpaRepository<Resume, Long> {

}
