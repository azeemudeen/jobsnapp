package com.jobsnapp.controllers;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.jobsnapp.exceptions.UserNotFoundException;
import com.jobsnapp.model.Job;
import com.jobsnapp.repositories.JobsRepository;
import com.jobsnapp.services.FilterApplicantService;

@RestController
public class FilterResumeController {
	
	@Autowired
	JobsRepository jobsRepository;
	
	@Autowired
	FilterApplicantService filterApplicantService;
	
	@PostMapping("/filter-applicants/{id}")
	public ResponseEntity<Job> filterApplicants(@PathVariable("id") long jobid,@RequestBody ArrayList<Long> filteredUserList){
		filteredUserList.forEach(e-> System.out.println(e));
		Job job =  jobsRepository.findById(jobid).orElseThrow(() -> new UserNotFoundException("Job id:"+jobid+" not found"));
		
		if(job != null) {
			filterApplicantService.process(job.getUsersApplied(),filteredUserList,job);
			job.setFiltered(true);
			jobsRepository.save(job);
		}
		return ResponseEntity.ok(job);
	}
}
