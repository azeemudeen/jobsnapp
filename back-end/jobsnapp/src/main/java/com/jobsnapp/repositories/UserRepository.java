package com.jobsnapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.PathVariable;

import com.jobsnapp.enumerations.RoleType;
import com.jobsnapp.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.username  = :email ")
    User findUserByUsername(@PathVariable String email);

    @Query("SELECT u FROM User u WHERE u.username  = :email ")
    Optional<User> findByUsername(@PathVariable String email);

    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.name = :name ")
    List<User> findByRole(@PathVariable RoleType name);
    
//    @Query("SELECT u FROM User u WHERE u.username  = :name AND u.password = :password AND u.user_type = :user_type")
//    User findUserByUsernameAndUserType(@PathVariable("name") String name,@PathVariable("password") String password,@PathVariable("usertype") String user_type);
    Optional<User> findByUsernameAndUserType(String name,String user_type);
}
