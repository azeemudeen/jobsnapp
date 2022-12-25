package com.jobsnapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.PathVariable;

import com.jobsnapp.model.Connection;
import com.jobsnapp.model.Notification;
import com.jobsnapp.model.User;

import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long>  {

    @Query("SELECT n FROM Notification n WHERE n.connection_request.id  = :id ")
    Optional<Notification> findByConnectionId(@PathVariable Long id);
}
