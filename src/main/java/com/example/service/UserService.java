package com.example.service;

import com.example.dto.UserDTO;
import com.example.model.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserService {
    List<UserDTO> findAllUsers();

    Optional<UserDTO> findUserById(String id) throws Exception;

    UserDTO saveUser(UserDTO userDTO);

    Map<String, Boolean> deleteUser(String userId) throws Exception;
}
