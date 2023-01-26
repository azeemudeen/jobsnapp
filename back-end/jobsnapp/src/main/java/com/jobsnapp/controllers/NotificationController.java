package com.jobsnapp.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.jobsnapp.model.*;
import com.jobsnapp.repositories.UserNotificationRepository;

@RestController
public class NotificationController {

	@Autowired
	private UserNotificationRepository userNotificationRepository;
	
	@GetMapping("/notification/{id}")
	public ResponseEntity<List<UserNotification>> getUserNotification(@PathVariable("id") long id){
		List<UserNotification> userNotifications = userNotificationRepository.findByUserId(id);
		return ResponseEntity.ok(userNotifications);
	}
    
}
