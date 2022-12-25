package com.jobsnapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.PathVariable;

import com.jobsnapp.enumerations.RoleType;
import com.jobsnapp.model.Role;
import com.jobsnapp.model.User;

public interface RoleRepository extends JpaRepository<Role, Long>  {
    @Query("SELECT r FROM Role r WHERE r.name  = :rn ")
    Role findByName(@PathVariable RoleType rn);
}
