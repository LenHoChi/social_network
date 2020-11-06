package com.example.service.impl;

import com.example.model.Users;
import com.example.repository.UsersRepository;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UsersRepository usersRepository;
    @Override
    public List<Users> getAllUsers() {
        return usersRepository.findAll();
    }

    @Override
    public Users saveUser(Users users) {
        return usersRepository.save(users);
    }

    @Override
    public Users updateUser(Users users, Integer id) {
        Users users1 = usersRepository.findById(id).get();
        users1.setUserName(users.getUserName());
        final Users updateUser = usersRepository.save(users1);
        return updateUser;
    }

    @Override
    public Map<String, Boolean> deleteUser(Integer userId) {
        Users users = usersRepository.findById(userId).get();
        usersRepository.delete(users);
        Map<String, Boolean> response = new HashMap<>();
        response.put("delete ok", Boolean.TRUE);
        return response;
    }
}
