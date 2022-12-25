package com.jobsnapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

import com.jobsnapp.model.Post;
import com.jobsnapp.model.User;

@Data
@AllArgsConstructor
public class FeedDTO {

    private User userDetails;
    private Set<Post> posts;
    private Set<User> connectedUsers;


}
