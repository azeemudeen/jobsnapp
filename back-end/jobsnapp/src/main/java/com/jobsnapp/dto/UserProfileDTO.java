package com.jobsnapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

import com.jobsnapp.model.SkillsAndExperience;
import com.jobsnapp.model.User;

@Data
@AllArgsConstructor
public class UserProfileDTO {
    private Long id;
    private String name;
    private String surname;
    private String position;
    private String company;
    private Boolean isConnected;
    private Set<User> network;
    private Set<SkillsAndExperience> skills;

}
