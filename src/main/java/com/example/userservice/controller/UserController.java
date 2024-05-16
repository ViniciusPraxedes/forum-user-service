package com.example.userservice.controller;

import com.example.userservice.model.ChangePictureDTO;
import com.example.userservice.model.ForumUser;
import com.example.userservice.model.ForumUserRequest;
import com.example.userservice.model.LoginRequest;
import com.example.userservice.service.UserService;
import jakarta.validation.Valid;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
@RestController
@CrossOrigin("*")
@RequestMapping("/user")
public class UserController {


    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profilePic/{userId}")
    public String getProfilePic(@PathVariable String userId){
        return userService.getProfilePic(userId);
    }
    @PutMapping("/updateProfilePicture")
    public void changeProfilePic(@RequestBody ChangePictureDTO request){
        userService.updateProfilePic(request);
    }




    @GetMapping("/{userId}")
    public ForumUser getUser(@PathVariable String userId){
        return userService.getUser(userId);
    }






    @PostMapping("/register")
    public ForumUser registerUser(@Valid @RequestBody ForumUserRequest request){
        return userService.registerUser(request);
    }

    @PostMapping("/login")
    public ForumUser loginUser(@Valid @RequestBody LoginRequest request){
        return userService.loginUser(request);
    }





}
