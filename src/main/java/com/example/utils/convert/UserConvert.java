package com.example.utils.convert;

import com.example.dto.UserDTO;
import com.example.model.User;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

public class UserConvert {
    static ModelMapper modelMapper = new ModelMapper();
    public static UserDTO modelToDTO(User user){
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        return userDTO;
    }
    public static List<UserDTO> listModelToListDTO(List<User> list){
        List<UserDTO> list1 = new ArrayList<>();
        for(int i=0; i<list.size();i++){
            UserDTO userDTO = modelMapper.map(list.get(i), UserDTO.class);
            list1.add(userDTO);
        }
        return list1;
    }
    public static User DTOToModel(UserDTO userDTO){
        User user = new User();
        user.setEmail(userDTO.getEmail());
        return user;
    }
}
