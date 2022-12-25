package com.jobsnapp.controllers;


import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jobsnapp.model.Picture;
import com.jobsnapp.model.User;
import com.jobsnapp.repositories.RoleRepository;
import com.jobsnapp.repositories.UserRepository;

import static com.jobsnapp.utils.PictureSave.decompressBytes;

import java.util.List;
import java.util.Set;

@RestController
@AllArgsConstructor
public class AdminController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;


    @Autowired
    private final BCryptPasswordEncoder encoder;

    @CrossOrigin(origins = "*") // CrossOrigin: For connecting with angular
    @GetMapping("/admin/users")
    public List<User> all() {
        List<User> users = userRepository.findAll();
        for(User u: users){
            System.out.println(u.getName());
            Picture uPic = u.getProfilePicture();
            if(uPic!=null && uPic.isCompressed()) {
                Picture temp = new Picture(uPic.getName(), uPic.getType(), decompressBytes(uPic.getBytes()));
                temp.setCompressed(false);
                u.setProfilePicture(temp);
            }
        }
        return users;
    }


}
