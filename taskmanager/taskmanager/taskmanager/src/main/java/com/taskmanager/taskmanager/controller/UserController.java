package com.taskmanager.taskmanager.controller;

import com.taskmanager.taskmanager.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.taskmanager.taskmanager.entity.User;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @PostMapping
    public User createUser(@Valid @RequestBody User user){
        return userRepository.save(user);
    }

    @GetMapping
    public java.util.List<User> getAllUser(){
        return userRepository.findAll();
    }




}
