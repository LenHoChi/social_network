package com.example.controller;

import com.example.model.User;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    private UserService userService;
    @GetMapping("/users")
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }
    @PostMapping("/users")
    public User createUser(@Valid @RequestBody User users){
        return userService.saveUser(users);
    }
    @DeleteMapping("/users/{id}")
    public Map<String, Boolean> deleteUser(@PathVariable(value = "id") String id){
        return userService.deleteUser(id);
    }
    @GetMapping("/users/{id}")
    public User getUserById(@PathVariable(value = "id") String id){
        return userService.getUserById(id).get();
    }
    @PutMapping("/users/{id}")
    public User updateUser(@PathVariable(value = "id") Integer id, @Valid @RequestBody User users){
        return userService.updateUser(users,id);
    }
}
