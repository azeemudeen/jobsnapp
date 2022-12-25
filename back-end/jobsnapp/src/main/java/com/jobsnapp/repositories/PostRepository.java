package com.jobsnapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jobsnapp.model.Post;
import com.jobsnapp.model.Role;

public interface PostRepository extends JpaRepository<Post, Long> {
}
