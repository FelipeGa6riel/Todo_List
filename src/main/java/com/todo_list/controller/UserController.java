package com.todo_list.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.todo_list.repositories.IUserRepository;
import com.todo_list.user.User;

import at.favre.lib.crypto.bcrypt.BCrypt;

@RestController
@RequestMapping("/users")
public class UserController {
    
    @Autowired
    IUserRepository userRepository;

    @PostMapping("/")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        
        var User = userRepository.findByUserName(user.getUserName());
        System.out.println("DATA = "+User);

        if(User != null ) {
           throw new RuntimeException("Usuario ja existe");
        }
        
        var passwordHashered = BCrypt.withDefaults().hashToString(12, user.getPassword().toCharArray());
        user.setPassword(passwordHashered);
        
        var userCreate = userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(userCreate);
    }
}
