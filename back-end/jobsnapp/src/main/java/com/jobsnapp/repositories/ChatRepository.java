package com.jobsnapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.PathVariable;

import com.jobsnapp.enumerations.RoleType;
import com.jobsnapp.model.Chat;
import com.jobsnapp.model.Role;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    /*@Query("SELECT c FROM Chat c JOIN User u WHERE u.id = :id AND u.id  ")
    Chat findChatByUsers(@PathVariable Long id, @PathVariable Long otherUserId);
    */
}
