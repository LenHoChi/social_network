package com.example;

import com.example.model.User;
import com.example.repository.UserRepository;
import com.example.service.impl.UserServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TodoServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAllDomains() throws Exception {
        User user = new User("len3");
        //User user1 = new User("len11");

        List<User> userList = Arrays.asList(user);

        when(userRepository.findAll()).thenReturn(userList);
        List<User> userList1 = userServiceImpl.getAllUsers();
        assertEquals(userList.size(), userList1.size());
    }
}
