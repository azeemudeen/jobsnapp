package com.jobsnapp.services;

import java.util.ArrayList;
import java.util.Set;

import com.jobsnapp.model.Job;
import com.jobsnapp.model.User;

public interface FilterApplicantService {
	public void process(Set<User> userApplied, ArrayList<Long> filteredUserList, Job job);
}
