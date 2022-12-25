package com.jobsnapp.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.jobsnapp.model.User;
import com.jobsnapp.repositories.UserRepository;

@Component
public class GuardUser {

    @Autowired
    UserRepository userRepository;

    public boolean checkUserId(Authentication authentication, int id) {
        String name = authentication.getName();
        User result = userRepository.findUserByUsername(name);
        return result != null && result.getId() == id;
    }
}
