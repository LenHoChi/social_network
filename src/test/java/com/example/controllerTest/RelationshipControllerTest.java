package com.example.controllerTest;

import com.example.controller.RelationshipController;
import com.example.controller.UserController;
import com.example.dto.RelationshipDTO;
import com.example.dto.UserDTO;
import com.example.model.Relationship;
import com.example.model.RelationshipPK;
import com.example.model.User;
import com.example.repository.RelationshipRepository;
import com.example.service.RelationshipService;
import com.example.service.UserService;
import com.example.utils.request.RequestFriends;
import com.example.utils.request.RequestFriendsList;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringRunner.class)
@WebMvcTest(RelationshipController.class)
public class RelationshipControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private RelationshipService relationshipService;

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testGetAllRelationships() throws Exception {
        RelationshipPK relationshipPK = new RelationshipPK("len1", "len10");
        RelationshipDTO relationshipDTO = new RelationshipDTO(relationshipPK, true, false, false);

        RelationshipPK relationshipPK1 = new RelationshipPK("len2", "len10");
        RelationshipDTO relationshipDTO1 = new RelationshipDTO(relationshipPK1, true, false, false);

        List<RelationshipDTO> relationshipDTOList = Arrays.asList(relationshipDTO, relationshipDTO1);
        //given(relationshipService.getAllRelationships()).willReturn(relationshipDTOList);
        when(relationshipService.getAllRelationships()).thenReturn(relationshipDTOList);
        mockMvc.perform(get("/api/relationship"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].relationshipPK.userEmail", is("len1")))
                .andExpect(jsonPath("$[1].relationshipPK.userEmail", is("len2")))
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$", hasSize(2)));
        verify(relationshipService, times(1)).getAllRelationships();
        verifyNoMoreInteractions(relationshipService);
    }
//loi 404 xay ra khi co the url sai
    @Test
    public void testGetRelationship() throws Exception {
        RelationshipPK relationshipPK = new RelationshipPK("len1", "len10");
        RelationshipDTO relationshipDTO = new RelationshipDTO(relationshipPK, true, false, false);
        when(relationshipService.getRelationshipById(relationshipPK)).thenReturn(Optional.of(relationshipDTO));
        mockMvc.perform(post("/api/relationship/getRelationshipbyId")
                .content(asJsonString(relationshipPK))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.relationshipPK.userEmail", is("len1")))
                .andExpect(content().contentType("application/json"));
        verify(relationshipService, times(1)).getRelationshipById(relationshipPK);
        verifyNoMoreInteractions(relationshipService);
    }

    @Test
    public void testBeFriends() throws Exception {
        List<String> listEmail = new ArrayList<>();
        listEmail.add("len1");
        listEmail.add("len10");
        RequestFriends requestFriends = new RequestFriends(listEmail);

        User user1 = new User("len1");
        User user2 = new User("len10");

        when(relationshipService.beFriends(user1.getEmail(), user2.getEmail())).thenReturn(true);
//        when(relationshipService.beFriends(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        mockMvc.perform(post("/api/relationship")
                .content(asJsonString(requestFriends))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));
        verify(relationshipService, times(1)).beFriends(user1.getEmail(), user2.getEmail());
        verifyNoMoreInteractions(relationshipService);
    }

    @Test
    public void testGetFriendsList() throws Exception {
        User user1 = new User("len1");
        RequestFriendsList requestFriendsList = new RequestFriendsList(user1.getEmail());

        List<String> listEmail = new ArrayList<>();
        listEmail.add("len1");
        listEmail.add("len10");

        when(relationshipService.getFriendsList(requestFriendsList.getEmail())).thenReturn(listEmail);
        mockMvc.perform(post("/api/relationship/friendsList")
                .content(asJsonString(requestFriendsList))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));
        verify(relationshipService, times(1)).getFriendsList(requestFriendsList.getEmail());
        verifyNoMoreInteractions(relationshipService);
    }
}
