package com.example.service.impl;

import com.example.dto.UserDTO;
import com.example.model.User;
import com.example.repository.UserRepository;
import com.example.service.UserService;
import com.example.utils.convert.UserConvert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Transactional(rollbackFor = Exception.class)
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository usersRepository;

    @Override
    public List<UserDTO> findAllUsers() {
        return UserConvert.convertListModelToListDTO(usersRepository.findAll());
    }

    @Override
    public Optional<UserDTO> findUserById(String id) throws Exception {
        return Optional.of(usersRepository.findById(id).map(UserConvert::convertModelToDTO).orElseThrow(() -> new Exception("Error")));

//        if(usersRepository.findById(id).isPresent()){
//            return Optional.ofNullable(UserConvert.modelToDTO(usersRepository.findById(id).get()));
//        }else{
//            throw new Exception("error");
////            throw new NoSuchFieldException();
//        }
    }

    private boolean checkSimilar(String email) {
        List<User> lstUser = usersRepository.findAll();
        for (int i = 0; i < lstUser.size(); i++) {
            if (lstUser.get(i).getEmail().equalsIgnoreCase(email)) {
                return false;
            }
        }
        return true;
    }
    //---> kết luận @valid chỉ xài cho input
    //còn @email đặt ở model thì gọi khi save nhung no ko bug ra loi trong message
    @Override
    public UserDTO saveUser(UserDTO userDTO) throws Exception {
        if (checkSimilar(userDTO.getEmail())) {
            return UserConvert.convertModelToDTO(usersRepository.save(UserConvert.convertDTOToModel(userDTO)));
        } else {
            throw new Exception("error (cause this email already)");
        }
    }

    @Override
    public Map<String, Boolean> deleteUser(String userId) throws Exception {
        if (usersRepository.findById(userId).isPresent()) {
            User users = usersRepository.findById(userId).get();
            usersRepository.delete(users);
            Map<String, Boolean> response = new HashMap<>();
            response.put("delete ok", Boolean.TRUE);
            return response;
        } else {
            throw new Exception("error (cause this email not exists)");
        }
    }
}
