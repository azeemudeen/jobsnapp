package com.jobsnapp.controllers;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.jobsnapp.exceptions.ObjectExistsException;
import com.jobsnapp.exceptions.PostNotFoundException;
import com.jobsnapp.exceptions.UserNotFoundException;
import com.jobsnapp.model.*;
import com.jobsnapp.recommendation.RecommendationAlgos;
import com.jobsnapp.repositories.CommentRepository;
import com.jobsnapp.repositories.JobsRepository;
import com.jobsnapp.repositories.PostRepository;
import com.jobsnapp.repositories.UserRepository;
import com.jobsnapp.services.UserService;

import static com.jobsnapp.utils.PictureSave.decompressBytes;

import java.util.*;

@RestController
@AllArgsConstructor
public class JobsController {

    @Autowired
    UserService userService;

    private final UserRepository userRepository;
    private final JobsRepository jobRepository ;
    private final CommentRepository commentRepository;

    @CrossOrigin(origins = "*")
    @PostMapping("/in/{id}/new-job")
    public ResponseEntity newJob(@PathVariable Long id, @RequestBody Job job) {
        System.out.println(job);
        User currentUser = userRepository.findById(id).orElseThrow(()->new UserNotFoundException("User with "+id+" not found"));
        job.setUserMadeBy(currentUser);
        jobRepository.save(job);
        return ResponseEntity.ok("\"Job created with success!\"");
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/in/{id}/jobs")
    public Set<Job> getJobs(@PathVariable Long id) {

        User currentUser = userRepository.findById(id).orElseThrow(()->new UserNotFoundException("User with "+id+" not found"));
        Set<Job> jobs = new HashSet<>();

        Set<Job> allJobs = new HashSet<>(jobRepository.findAll());
        jobs.addAll(allJobs);


        for(Job j: jobs) {

            User owner = j.getUserMadeBy();

            Picture pic = owner.getProfilePicture();
            if(pic != null){
                if(pic.isCompressed()){
                    Picture tempPicture = new Picture(pic.getId(),pic.getName(),pic.getType(),decompressBytes(pic.getBytes()));
                    pic.setCompressed(false);
                    owner.setProfilePicture(tempPicture);
                }
            }

            Set<User> usersApplied = j.getUsersApplied();
            for(User u: usersApplied) {
                Picture cpic = u.getProfilePicture();
                if(cpic != null){
                    if(cpic.isCompressed()){
                        Picture tempPicture = new Picture(cpic.getId(),cpic.getName(),cpic.getType(),decompressBytes(cpic.getBytes()));
                        cpic.setCompressed(false);
                        u.setProfilePicture(tempPicture);
                    }
                }
            }
        }



        return jobs;
    }

    @CrossOrigin(origins = "*")
    @PutMapping("/in/{id}/jobs/make-application/{jobId}")
    public ResponseEntity newApplication(@PathVariable Long id, @PathVariable Long jobId) {

        System.out.println("\n\nnewApplication\n");
        User currentUser = userRepository.findById(id).orElseThrow(()->new UserNotFoundException("User with "+id+" not found"));
        Job job = jobRepository.findById(jobId).orElseThrow(()->new UserNotFoundException("Job not found"));
        Set<User> usersApplied = job.getUsersApplied();
        if(!usersApplied.contains(currentUser)){
            usersApplied.add(currentUser);
            job.setUsersApplied(usersApplied);
            jobRepository.save(job);
        }else
            return ResponseEntity
                    .badRequest()
                    .body("{\"timestamp\": " + "\"" + new Date().toString() + "\","
                            + "\"status\": 400, "
                            + "\"error\": \"Bad Request\", "
                            + "\"message\": \"Application has already been made!\", "
                    );

        return ResponseEntity.ok("\"Interested in post created with success!\"");
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/in/{id}/jobs/{jobId}/applicants")
    public Set<User> getJobApplicants(@PathVariable Long id, @PathVariable Long jobId) {
        User currentUser = userRepository.findById(id).orElseThrow(()->new UserNotFoundException("User with "+id+" not found"));
        Job job = jobRepository.findById(jobId).orElseThrow(()->new UserNotFoundException("Job not found"));
        return job.getUsersApplied();
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/in/{id}/recommended-jobs")
    public List<Job> getRecommendedJobs(@PathVariable Long id){
        RecommendationAlgos recAlgos = new RecommendationAlgos();
        recAlgos.recommendedJobs(userRepository, jobRepository, userService);
        User currentUser = userRepository.findById(id).orElseThrow(()->new UserNotFoundException("User with "+id+" not found"));
        List<Job> recommendedJobs = new ArrayList<>();
        if (currentUser.getRecommendedJobs().size() != 0){
            System.out.println("list is null");
            recommendedJobs = currentUser.getRecommendedJobs();
        } else {
            return new ArrayList<>(getJobs(id));
        }

        for (Job j:recommendedJobs  ) {
            System.out.println(j);
        }
        System.out.println(recommendedJobs);
        Collections.reverse(recommendedJobs);

        for(Job j: recommendedJobs) {
            System.out.println(j);

            User owner = j.getUserMadeBy();

            Picture pic = owner.getProfilePicture();
            if(pic != null){
                if(pic.isCompressed()){
                    Picture tempPicture = new Picture(pic.getId(),pic.getName(),pic.getType(),decompressBytes(pic.getBytes()));
                    tempPicture.setCompressed(false);
                    owner.setProfilePicture(tempPicture);
                }
            }

            Set<User> usersApplied = j.getUsersApplied();
            for(User u: usersApplied) {
                Picture cpic = u.getProfilePicture();
                if(cpic != null){
                    if(cpic.isCompressed()){
                        Picture tempPicture = new Picture(cpic.getId(),cpic.getName(),cpic.getType(),decompressBytes(cpic.getBytes()));
                        tempPicture.setCompressed(false);
                        u.setProfilePicture(tempPicture);
                    }
                }
            }
        }

        return recommendedJobs;
    }


}
