package com.jobsnapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jobsnapp.model.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
