package com.jobsnapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.jobsnapp.model.Picture;
import com.jobsnapp.model.Resume;
import com.jobsnapp.model.User;
import com.jobsnapp.repositories.UserRepository;

@RestController
public class ResumeController {
	
	@Autowired
	UserRepository userRepository;
	
	@GetMapping("/resume/{id}")
	public ResponseEntity<Resource> getResume(@PathVariable("id") long id) {
		User user = userRepository.findById(id).orElse(null);
		Resume resumeFile = user.getResumeFile();
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(resumeFile.getType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resumeFile.getName() + "\"")
                .body(new ByteArrayResource(resumeFile.getBytes()));
	}

}
