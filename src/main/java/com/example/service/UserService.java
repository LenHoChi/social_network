package com.example.service;

import com.example.model.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserService {
    List<User> getAllUsers();
    Optional<User> getUserById(String id);
    User saveUser(User users);
    User updateUser(User users, Integer id);
    Map<String, Boolean> deleteUser(String userId);
}
