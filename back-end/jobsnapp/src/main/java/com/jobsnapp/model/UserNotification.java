package com.jobsnapp.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Table(name = "user_notification")
public class UserNotification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "message") @NonNull
    private String message;
    
    @Column(name = "timestamp") @NonNull
    private Date timestamp;
    
    @ManyToOne @NonNull
    @JsonIgnoreProperties(value = {"phoneNumber","roles","password","passwordConfirm","resumeFile","profilePicture","messages","chats","jobApplied","jobsCreated","comments", "posts","usersFollowing","userFollowedBy","posts","comments","notifications","interestReactions","jobsCreated","interactions","jobApplied","messages","chats"},allowSetters = true)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private User user;
    
    @Column(name = "is_positive") @NonNull
    private boolean isPositive;
    
    @Column(name = "title") @NonNull
    private String title;
}
