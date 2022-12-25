package com.jobsnapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

import com.jobsnapp.model.Comment;
import com.jobsnapp.model.Connection;
import com.jobsnapp.model.InterestReaction;

@Data
@AllArgsConstructor
public class NotificationsDTO {
    private Set<NetworkUserDTO> connectionsPending;
    private Set<InterestReaction> interestReactions;
    private Set<Comment> comments;
}
