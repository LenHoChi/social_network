package com.example.controller;

import com.example.model.Users;
import com.example.service.FriendService;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    private UserService userService;
    @GetMapping("/users")
    public List<Users> getAllUsers(){
        return userService.getAllUsers();
    }
    @PostMapping("/users")
    public Users createUser(Users users){
        return userService.saveUser(users);
    }
}
