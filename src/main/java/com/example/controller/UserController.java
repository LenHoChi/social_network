package com.example.controller;

import com.example.dto.UserDTO;
import com.example.model.User;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("")
    public ResponseEntity<?> findAllUsers(){
        List<UserDTO> listUserDTO = userService.findAllUsers();
        if(listUserDTO.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(userService.findAllUsers());
    }

    @PostMapping("")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserDTO userDTO) throws Exception {
        return ResponseEntity.ok(userService.saveUser(userDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteUser(@PathVariable(value = "id") String id) throws Exception {
        return new ResponseEntity<>(userService.deleteUser(id), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findUserById(@PathVariable(value = "id") String id) throws Exception {
        return ResponseEntity.ok(userService.findUserById(id));
    }
}
