package com.example.service.impl;

import com.example.model.User;
import com.example.repository.UserRepository;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository usersRepository;

    @Override
    public List<User> getAllUsers() {
        return usersRepository.findAll();
    }

    @Override
    public Optional<User> getUserById(String id) {
        return usersRepository.findById(id);
    }

    @Override
    public User saveUser(User users) {
        return usersRepository.save(users);
    }

    @Override
    public Map<String, Boolean> deleteUser(String userId) {
        User users = usersRepository.findById(userId).get();
        usersRepository.delete(users);
        Map<String, Boolean> response = new HashMap<>();
        response.put("delete ok", Boolean.TRUE);
        return response;
    }
}
