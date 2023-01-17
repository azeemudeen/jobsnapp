package com.jobsnapp.controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.jobsnapp.dto.NewUserInfo;
import com.jobsnapp.enumerations.RoleType;
import com.jobsnapp.enumerations.SkillType;
import com.jobsnapp.exceptions.UserNotFoundException;
import com.jobsnapp.model.Picture;
import com.jobsnapp.model.Resume;
import com.jobsnapp.model.Role;
import com.jobsnapp.model.SkillsAndExperience;
import com.jobsnapp.model.User;
import com.jobsnapp.repositories.RoleRepository;
import com.jobsnapp.repositories.SkillsAndExperienceRepository;
import com.jobsnapp.repositories.UserRepository;
import com.jobsnapp.security.SecurityConstants;

import lombok.AllArgsConstructor;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.jobsnapp.utils.PictureSave.*;

import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

@RestController
@AllArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final SkillsAndExperienceRepository skillsAndExperienceRepository;

    @Autowired
    private final BCryptPasswordEncoder encoder;

    @CrossOrigin(origins = "*")
    @PostMapping(value = "/signup", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> signup(@RequestPart("object") User user, @RequestPart(value = "imageFile") MultipartFile file, @RequestPart(value = "resumeFile",required = false) MultipartFile resumefile) throws IOException {
    	if(userRepository.findUserByUsername(user.getUsername()) == null) {
            if (user.getPassword().equals(user.getPasswordConfirm())) {
            	
        		String phone = user.getPhoneNumber();
            	if(phone != null && StringUtils.isNumeric(phone) && phone.length() != 10) {
            		return ResponseEntity
                            .badRequest()
                            .body("{\"timestamp\": " + "\"" + new Date().toString() + "\","
                                    + "\"status\": 400, "
                                    + "\"error\": \"Bad Request\", "
                                    + "\"message\": \"Phone number must be 10 digits!\"}"
                            );
            	}
            	
            	String email = user.getUsername();
            	boolean isValidEmail = Pattern.compile("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@" 
            	        + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$").matcher(email).matches();
            	if(email != null && !isValidEmail) {
            		return ResponseEntity
            				.badRequest()
            				.body("{\"timestamp\": " + "\"" + new Date().toString() + "\","
            						+ "\"status\": 400, "
            						+ "\"error\": \"Bad Request\", "
            						+ "\"message\": \"Invalid Email address!\"}"
            						);
            	}
                user.setPassword(encoder.encode(user.getPassword()));
                Set<Role> roles = new HashSet<>();
                Role r = roleRepository.findByName(RoleType.PROFESSIONAL);
                roles.add(r);
                user.setRoles(roles);
                if(file!=null){
                    Picture pic = new Picture(file.getOriginalFilename() ,file.getContentType() ,compressBytes(file.getBytes()));
                    pic.setCompressed(true);
                    user.setProfilePicture(pic);
                    System.out.println("> Picture saved");
                }
                if(resumefile!=null) {
                	Resume resume = new Resume(resumefile.getOriginalFilename(), resumefile.getContentType(), resumefile.getBytes());
                	user.setResumeFile(resume);
                } else if(user.getUserType().equals("C")){
                	return ResponseEntity
            				.badRequest()
            				.body("{\"timestamp\": " + "\"" + new Date().toString() + "\","
            						+ "\"status\": 400, "
            						+ "\"error\": \"Bad Request\", "
            						+ "\"message\": \"Resume is mandatory!\"}"
            						);
                }

                userRepository.save(user);
                System.out.println("> New user signed up");
            } else
                return ResponseEntity
                        .badRequest()
                        .body("{\"timestamp\": " + "\"" + new Date().toString() + "\","
                                + "\"status\": 400, "
                                + "\"error\": \"Bad Request\", "
                                + "\"message\": \"Passwords do not match!\"}"
                        );
        } else
            return ResponseEntity
                    .badRequest()
                    .body("{\"timestamp\": " + "\"" + new Date().toString() + "\","
                            + "\"status\": 400, "
                            + "\"error\": \"Bad Request\", "
                            + "\"message\": \"Email exists already!\"}"
                    );

        String token = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(SecurityConstants.SECRET.getBytes()));
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token);
        responseHeaders.set("Content-Type","application/json");
        user.setPassword(null);

        return ResponseEntity.ok().headers(responseHeaders).body(user);
    }
	

	@CrossOrigin(origins = "*")
    @GetMapping("/in/{id}")
    public User getProfile(@PathVariable Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User with id "+id+"doesn't exist"));
        Picture pic = user.getProfilePicture();
        if(pic != null && pic.isCompressed()){
            Picture tempPicture = new Picture(pic.getId(),pic.getName(),pic.getType(),decompressBytes(pic.getBytes()));
            tempPicture.setCompressed(false);
            user.setProfilePicture(tempPicture);
        }
        return user;
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/in/{id}/user-profile/{otherId}")
    public User getPersonalProfile(@PathVariable Long id,@PathVariable Long otherId) {
        User user = userRepository.findById(otherId).orElseThrow(() -> new UserNotFoundException("User with id "+id+"doesn't exist"));
        Picture pic = user.getProfilePicture();
        if(pic != null && pic.isCompressed()){
            Picture tempPicture = new Picture(pic.getId(),pic.getName(),pic.getType(),decompressBytes(pic.getBytes()));
            pic.setCompressed(false);
            user.setProfilePicture(tempPicture);
        }
        return user;
    }

    @CrossOrigin(origins = "*")
    @PutMapping("/in/{id}/profile/new-info")
    public ResponseEntity<String> informPersonalProfile(@PathVariable Long id, @RequestBody SkillsAndExperience skill) {
        System.out.println("\n\n> informPersonalProfile");
        System.out.println(skill);
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User with id "+id+"doesn't exist"));
        if(skill.getType() == SkillType.EXPERIENCE){
            skill.setUserExp(user);
        } else if(skill.getType() == SkillType.SKILL) {
            skill.setUserSk(user);
        } else if(skill.getType() == SkillType.EDUCATION) {
            skill.setUserEdu(user);
        }

        skillsAndExperienceRepository.save(skill);
        System.out.println(user);
        System.out.println("> All changes made with success!");
        return ResponseEntity.ok("\"All changes made with success!\"");
    }

    @CrossOrigin(origins = "*")
    @PutMapping("/in/{id}/editJob")
    public ResponseEntity<String> editUserJob(@PathVariable Long id, @RequestBody User user) {

        System.out.println("\n\n> Edit User's Job");
        User newUser = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User with id "+id+"doesn't exist"));

        if(user.getCurrentJob()!= null )
            newUser.setCurrentJob(user.getCurrentJob());
        if(user.getCurrentCompany()!= null)
            newUser.setCurrentCompany(user.getCurrentCompany());
        if(user.getCity()!= null)
            newUser.setCity(user.getCity());
        if(user.getWebsite()!= null )
            newUser.setWebsite(user.getWebsite());
        if(user.getGithub()!= null )
            newUser.setGithub(user.getGithub());
        if(user.getTwitter()!= null )
            newUser.setTwitter(user.getTwitter());

        userRepository.save(newUser);
        return ResponseEntity.ok("\"All changes made with success!\"");
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/in/{id}/profile/{otherUserId}")
    public User showProfile(@PathVariable Long id, @PathVariable Long otherUserId) {
        userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User with id "+id+"doesn't exist"));
        User userPreview = userRepository.findById(otherUserId).orElseThrow(() -> new UserNotFoundException("User with id "+otherUserId+"doesn't exist"));
        Picture pic = userPreview.getProfilePicture();
        if(pic != null && pic.isCompressed()){
            Picture tempPicture = new Picture(pic.getId(),pic.getName(),pic.getType(),decompressBytes(pic.getBytes()));
            userPreview.setProfilePicture(tempPicture);
            pic.setCompressed(false);
        }
        return userPreview;
    }

    @CrossOrigin(origins = "*")
    @PutMapping("/in/{id}/settings/change-password")
    public ResponseEntity<String> changePassword(@PathVariable Long id , @RequestBody NewUserInfo pwdDetails) {
    	String email = pwdDetails.getNewUsername();
    	boolean isValidEmail = Pattern.compile("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@" 
    	        + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$").matcher(email).matches();
    	if(email != null && !isValidEmail) {
    		return ResponseEntity
    				.badRequest()
    				.body("{\"timestamp\": " + "\"" + new Date().toString() + "\","
    						+ "\"status\": 400, "
    						+ "\"error\": \"Bad Request\", "
    						+ "\"message\": \"Invalid Email address!\"}"
    						);
    	}
    	if (!pwdDetails.getNewPassword().equals(pwdDetails.getPasswordConfirm())) {
            System.out.println("\"Passwords do not match!\"");
            return ResponseEntity
                    .badRequest()
                    .body("{\"timestamp\": " + "\"" + new Date().toString()+ "\","
                            + "\"status\": 400, "
                            + "\"error\": \"Bad Request\", "
                            + "\"message\": \"Passwords do not match!\", "
                            + "\"path\": \"/users/"+ id.toString() +"/passwordchange\"}"
                    );
        }
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User with id "+id+"doesn't exist"));
        if(!encoder.matches(pwdDetails.getCurrentPassword(),user.getPassword())){
            System.out.println("\"Wrong password\"");
            return ResponseEntity
                    .badRequest()
                    .body("{\"timestamp\": " + "\"" + new Date().toString()+ "\","
                            + "\"status\": 400, "
                            + "\"error\": \"Bad Request\", "
                            + "\"message\": \"Wrong password!\", "
                            + "\"path\": \"/users/"+ id.toString() +"/passwordchange\"}"
                    );
        }

        user.setPassword(encoder.encode(pwdDetails.getNewPassword()));
        userRepository.save(user);
        System.out.println("\"Password Changed!\"");
        return ResponseEntity.ok("\"Password Changed!\"");
    }


    @CrossOrigin(origins = "*")
    @PutMapping("/in/{id}/settings/change-username")
    public ResponseEntity<String> changeUserName(@PathVariable Long id , @RequestBody NewUserInfo details) {
        String token = null;
        User existingUser = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User with id "+id+"doesn't exist"));

        if(!encoder.matches(details.getCurrentPassword(),existingUser.getPassword())){
            System.out.println("\"Wrong password\"");
            return ResponseEntity
                    .badRequest()
                    .body("{\"timestamp\": " + "\"" + new Date().toString()+ "\","
                            + "\"status\": 400, "
                            + "\"error\": \"Bad Request\", "
                            + "\"message\": \"Wrong password!\", "
                            + "\"path\": \"/users/"+ id.toString() +"/passwordchange\"}"
                    );
        }
        token = JWT.create()
                .withSubject(details.getNewUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(SecurityConstants.SECRET.getBytes()));
        token = SecurityConstants.TOKEN_PREFIX + token;
        existingUser.setUsername(details.getNewUsername());
        userRepository.save(existingUser);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(SecurityConstants.HEADER_STRING, token);
        return ResponseEntity.ok().headers(responseHeaders).body("\"Successful edit!\"");
    }

}
