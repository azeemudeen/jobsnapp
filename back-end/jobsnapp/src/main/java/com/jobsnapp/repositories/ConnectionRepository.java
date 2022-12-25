package com.jobsnapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jobsnapp.model.Connection;
import com.jobsnapp.model.Post;

public interface ConnectionRepository  extends JpaRepository<Connection, Long> {
}
