package com.jobsnapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jobsnapp.model.Comment;
import com.jobsnapp.model.Post;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
