package com.example.apiTest;

import com.example.controller.UserController;
import com.example.dto.UserDTO;
import com.example.model.User;
import com.example.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.catalina.filters.CorsFilter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;

import java.util.*;

import static org.mockito.BDDMockito.given;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import org.apache.catalina.filters.CorsFilter;
//@RunWith(MockitoJUnitRunner.class)
//@RunWith(SpringJUnit4ClassRunner.class)
@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    //@Mock
    private UserService userService;

//    @InjectMocks
//    private UserController userController;
//
//    @Before
//    public void setUp() throws Exception {
//        MockitoAnnotations.initMocks(this);
////        mockMvc = MockMvcBuilders.standaloneSetup(userController).addFilters(new CorsFilter()).build();
//        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
//    }
    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
//    @Test
//    public void testGetAllUsers() throws Exception {
//        UserDTO userDTOA = new UserDTO("len1");
//        UserDTO userDTOB = new UserDTO("len2");
//        List<UserDTO> userDTOList = Arrays.asList(userDTOA, userDTOB);
//        //when(userService.getAllUsers()).thenReturn(userDTOList);
//        given(userService.getAllUsers()).willReturn(userDTOList);
//        mockMvc.perform(get("/api/users"))
//                .andExpect(status().isOk())
//                //ko hieu?tim hieu sau
////                .andExpect((ResultMatcher) content().contentType("application/json"))
//                //.andExpect(jsonPath("$.message").value("success"))
//                .andExpect(jsonPath("$[0].email", is("len1")))
//                .andExpect(jsonPath("$[1].email", is("len2")));
//        verify(userService, times(1)).getAllUsers();
//    }
//    @Test
//    public void testGetUser() throws Exception{
//        UserDTO userDTO = new UserDTO("len1");
//        when(userService.getUserById(userDTO.getEmail())).thenReturn(Optional.of(userDTO));
//        mockMvc.perform(get("/api/users/{id}", userDTO.getEmail()))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.email", is("len1")));
//        verify(userService, times(1)).getUserById(userDTO.getEmail());
//    }
    @Test
    public void testCreateUser() throws Exception{
        UserDTO userDTO = new UserDTO("len1");
        when(userService.saveUser(Mockito.any(UserDTO.class))).thenReturn(userDTO);
//        doNothing().when(userService).saveUser(userDTO);
        mockMvc.perform(post("/api/users"))
                .andExpect(status().isCreated());

//        Mockito.when(userService.saveUser(Mockito.any(UserDTO.class))).thenReturn(userDTO);
//        RequestBuilder requestBuilder = MockMvcRequestBuilders
//                .post("/api/users");
//        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
//        MockHttpServletResponse response = result.getResponse();


//        assertEquals("http://localhost:8080/api/users",response.getHeader(HttpHeaders.LOCATION));
//        mockMvc.perform(post("/api/users"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.email", is("len1")));
       // verify(userService, times(1)).saveUser(userDTO);
    }


//    @Test
//    public void testDeleteUser() throws Exception{
//        UserDTO userDTO = new UserDTO("len1");
//        Map<String, Boolean> response = new HashMap<>();
//        response.put("delete ok", Boolean.TRUE);
////        doNothing().when(userService.deleteUser(userDTO.getEmail()));
//        when(userService.deleteUser(userDTO.getEmail())).thenReturn(response);
//        mockMvc.perform(delete("/api/users/{id}", userDTO.getEmail()))
//                .andExpect(status().isOk());
//        verify(userService, times(1)).deleteUser(userDTO.getEmail());
//    }

}
