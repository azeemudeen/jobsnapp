package com.jobsnapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.PathVariable;

import com.jobsnapp.model.InterestReaction;
import com.jobsnapp.model.Notification;
import com.jobsnapp.model.User;

import java.util.Optional;

public interface InterestReactionRepository extends JpaRepository<InterestReaction, Long>  {

    @Query("SELECT ir FROM InterestReaction ir WHERE ir.post.id  = :postId AND ir.userMadeBy.id = :userId ")
    Optional<InterestReaction> isInterested(@PathVariable Long userId,@PathVariable Long postId);

}
