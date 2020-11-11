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
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import java.util.Arrays;
import java.util.List;
import static org.mockito.BDDMockito.given;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.core.Is.is;
//@RunWith(MockitoJUnitRunner.class)
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
//        mockMvc = MockMvcBuilders.standaloneSetup(userController).addFilters(new CorsFilter()).build();
//    }
//    public static String asJsonString(final Object obj) {
//        try {
//            return new ObjectMapper().writeValueAsString(obj);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
    @Test
    public void testGetAllDomains() throws Exception {
        UserDTO userDTOA = new UserDTO("len1");
        UserDTO userDTOB = new UserDTO("len2");
        List<UserDTO> userDTOList = Arrays.asList(userDTOA, userDTOB);
        //when(userService.getAllUsers()).thenReturn(userDTOList);
        given(userService.getAllUsers()).willReturn(userDTOList);
        mockMvc.perform(get("/api/users"))
                //.andExpect(status().isOk())
                //.andExpect((ResultMatcher) content().contentType(MediaType.APPLICATION_JSON_UTF8))
                //.andExpect(jsonPath("$.message").value("success"))
                .andExpect(jsonPath("$[0].email", is("len1")))
                .andExpect(jsonPath("$[1].email", is("len2")));
        verify(userService, times(1)).getAllUsers();
        //verifyNoInteractions(userService);
    }
}
