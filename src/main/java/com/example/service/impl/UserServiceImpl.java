package com.example.service.impl;

import com.example.dto.UserDTO;
import com.example.exception.RelationshipException;
import com.example.exception.ResouceNotFoundException;
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

//propagation = Propagation.REQUIRED tao moi transaction neu chua co
//Propagation.REQUIRED_NEW tao moi transaction bat ke ly do
//@Transactional(readOnly = true) //ko cho sua
//@Transactional //tu dong update mean :@Transactional(rollbackFor = { RuntimeException.class, Error.class }) chi bat dung 2 thang nay. exception ko bat
//@Transactional(noRollbackFor = Exception.class)//ko rollback voi exception but voi error thi rollback
@Transactional(rollbackFor = Exception.class) //roll back voi all exception, error
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
        return Optional.of(usersRepository.findById(id).map(UserConvert::convertModelToDTO).orElseThrow(() -> new Exception("Error not found")));

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













//-------------test transaction-----------------------------------------------------------------------
    @Override
    public void testTransactionalException() throws Exception {
        User user1 = new User("zoombiev1@gmail.com");
        User user2 = new User("zoombiev2@gmail.com");
        usersRepository.save(user1);
        usersRepository.save(user2);
        this.demoException();

//        usersRepository.deleteById(user1.getEmail());
//        usersRepository.deleteById(user2.getEmail());
//        usersRepository.deleteById(user1.getEmail());
    }

    @Override
    public void testTransactionalNoException() throws Exception {
        User user1 = new User("zoombiev1@gmail.com");
        User user2 = new User("zoombiev2@gmail.com");
        usersRepository.save(user1);
        usersRepository.save(user2);
        this.demoException();
    }

    @Override
    public void testTransactionalReadOnly() throws Exception {
        User user1 = new User("zoombiev1@gmail.com");
        User user2 = new User("zoombiev2@gmail.com");
        usersRepository.save(user1);
        usersRepository.save(user2);
        usersRepository.save(user2);
    }

    @Override
    public void testTransactional() throws Exception {
//        Optional<User> user = usersRepository.findById("zoombiev1@gmail.com");
//        user.get().setEmail("len@gmail.com");
//        usersRepository.save(user.get());

        User user1 = new User("zoombiev1@gmail.com");
        User user2 = new User("zoombiev2@gmail.com");
        usersRepository.save(user1);
        usersRepository.save(user2);
        this.demoException();
    }

    public void demoException() throws Exception {
        throw new Exception("demo throw exception");
       //throw new Error("df");
        //throw new RuntimeException("dfdfd");

    }
}
