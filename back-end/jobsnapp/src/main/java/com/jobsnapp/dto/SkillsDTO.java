package com.jobsnapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

import com.jobsnapp.model.SkillsAndExperience;

@Data
@AllArgsConstructor
public class SkillsDTO {
    private Set<SkillsAndExperience> education;
    private Set<SkillsAndExperience> workExperience;
    private Set<SkillsAndExperience> skills;
}
