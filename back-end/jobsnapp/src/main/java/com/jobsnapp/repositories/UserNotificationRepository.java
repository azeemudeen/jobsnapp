package com.jobsnapp.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jobsnapp.model.UserNotification;

public interface UserNotificationRepository extends JpaRepository<UserNotification, Long>{
	public List<UserNotification> findByUserId(long id);
}
