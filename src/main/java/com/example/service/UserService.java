package com.example.service;

import com.example.model.Friends;
import com.example.model.Users;

import java.util.List;
import java.util.Map;

public interface UserService {
    List<Users> getAllUsers();
    Users saveUser(Users users);
    Users updateUser(Users users, Integer id);
    Map<String, Boolean> deleteUser(Integer userId);
}
