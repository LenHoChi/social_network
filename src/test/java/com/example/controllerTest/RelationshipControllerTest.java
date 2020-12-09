package com.example.controllerTest;

import com.example.controller.RelationshipController;
import com.example.dto.RelationshipDTO;
import com.example.model.Relationship;
import com.example.model.RelationshipPK;
import com.example.model.User;
import com.example.service.RelationshipService;
import com.example.service.UserService;
import com.example.model.request.RequestFriends;
import com.example.model.request.RequestFriendsList;
import com.example.model.request.RequestReciveUpdate;
import com.example.model.request.RequestSubcriber;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringRunner.class)
@WebMvcTest(RelationshipController.class)
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
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
    @WithMockUser(username = "len", password = "abc", roles = {"ADMIN"})
    @Test
    public void testGetAllRelationships() throws Exception {
        RelationshipPK relationshipPK = new RelationshipPK("len1", "len10");
        RelationshipDTO relationshipDTO = new RelationshipDTO(relationshipPK, true, false, false);

        RelationshipPK relationshipPK1 = new RelationshipPK("len2", "len10");
        RelationshipDTO relationshipDTO1 = new RelationshipDTO(relationshipPK1, true, false, false);

        List<RelationshipDTO> relationshipDTOList = Arrays.asList(relationshipDTO, relationshipDTO1);
        //given(relationshipService.getAllRelationships()).willReturn(relationshipDTOList);
        when(relationshipService.findAllRelationships()).thenReturn(relationshipDTOList);
        mockMvc.perform(get("/api/relationship"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].relationshipPK.userEmail", is("len1")))
                .andExpect(jsonPath("$[1].relationshipPK.userEmail", is("len2")))
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$", hasSize(2)));
        verify(relationshipService, times(2)).findAllRelationships();
        verifyNoMoreInteractions(relationshipService);
    }
    @Test
    public void testGetRelationship() throws Exception {
        RelationshipPK relationshipPK = new RelationshipPK("newmooncsu@gmail.com", "newmooncsu2@gmail.com");
        RelationshipDTO relationshipDTO = new RelationshipDTO(relationshipPK, true, false, false);
        when(relationshipService.findRelationshipById(relationshipPK)).thenReturn(Optional.of(relationshipDTO));
        mockMvc.perform(post("/api/relationship/find-relationship-by-id")
                .content(asJsonString(relationshipPK))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.relationshipPK.userEmail", is("newmooncsu@gmail.com")))
                .andExpect(content().contentType("application/json"));
        verify(relationshipService, times(1)).findRelationshipById(relationshipPK);
        verifyNoMoreInteractions(relationshipService);
    }
    @Test
    public void testGetRelationshipFailByEmailWrongFormat() throws Exception {
        RelationshipPK relationshipPK = new RelationshipPK("newmooncsu", "newmooncsu2");
        RelationshipDTO relationshipDTO = new RelationshipDTO(relationshipPK, true, false, false);
        when(relationshipService.findRelationshipById(relationshipPK)).thenReturn(Optional.of(relationshipDTO));
        mockMvc.perform(post("/api/relationship/find-relationship-by-id")
                .content(asJsonString(relationshipPK))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errors[0]", is("error email")))
                .andExpect(jsonPath("$.errors[1]", is("error email")))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"));
    }
    @Test
    public void testGetRelationshipFailByEmailNull() throws Exception {
        RelationshipPK relationshipPK = new RelationshipPK(null, null);
        RelationshipDTO relationshipDTO = new RelationshipDTO(relationshipPK, true, false, false);
        when(relationshipService.findRelationshipById(relationshipPK)).thenReturn(Optional.of(relationshipDTO));
        mockMvc.perform(post("/api/relationship/find-relationship-by-id")
                .content(asJsonString(relationshipPK))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"));
    }
    @Test
    public void testGetRelationshipFailByEmailEmpty() throws Exception {
        RelationshipPK relationshipPK = new RelationshipPK("", "");
        RelationshipDTO relationshipDTO = new RelationshipDTO(relationshipPK, true, false, false);
        when(relationshipService.findRelationshipById(relationshipPK)).thenReturn(Optional.of(relationshipDTO));
        mockMvc.perform(post("/api/relationship/find-relationship-by-id")
                .content(asJsonString(relationshipPK))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"));
    }
    @Test
    public void testBeFriends() throws Exception {
        List<String> listEmail = new ArrayList<>();
        listEmail.add("newmooncsu@gmail.com");
        listEmail.add("newmooncsu1@gmail.com");
        RequestFriends requestFriends = new RequestFriends(listEmail);

        User user1 = new User("newmooncsu@gmail.com");
        User user2 = new User("newmooncsu1@gmail.com");

        when(relationshipService.beFriends(user1.getEmail(), user2.getEmail())).thenReturn(true);
        mockMvc.perform(post("/api/relationship")
                .content(asJsonString(requestFriends))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(content().contentType("application/json"));
        verify(relationshipService, times(1)).beFriends(user1.getEmail(), user2.getEmail());
        verifyNoMoreInteractions(relationshipService);
    }
    @Test
    public void testBeFriendsFailByEmailWrongFormat() throws Exception {
        List<String> listEmail = new ArrayList<>();
        listEmail.add("newmooncsu");
        listEmail.add("newmooncsu1");
        RequestFriends requestFriends = new RequestFriends(listEmail);

        User user1 = new User("newmooncsu");
        User user2 = new User("newmooncsu1");

        when(relationshipService.beFriends(user1.getEmail(), user2.getEmail())).thenReturn(true);
        mockMvc.perform(post("/api/relationship")
                .content(asJsonString(requestFriends))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"));
    }
    @Test
    public void testBeFriendsFailByEmailNull() throws Exception {
        List<String> listEmail = new ArrayList<>();
        listEmail.add(null);
        listEmail.add(null);
        RequestFriends requestFriends = new RequestFriends(listEmail);

        User user1 = new User("newmooncsu");
        User user2 = new User("newmooncsu1");

        when(relationshipService.beFriends(user1.getEmail(), user2.getEmail())).thenReturn(true);
        mockMvc.perform(post("/api/relationship")
                .content(asJsonString(requestFriends))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"));
    }
    @Test
    public void testBeFriendsFailByEmailEmpty() throws Exception {
        List<String> listEmail = new ArrayList<>();
        listEmail.add("");
        listEmail.add("");
        RequestFriends requestFriends = new RequestFriends(listEmail);

        User user1 = new User("newmooncsu");
        User user2 = new User("newmooncsu1");

        when(relationshipService.beFriends(user1.getEmail(), user2.getEmail())).thenReturn(true);
        mockMvc.perform(post("/api/relationship")
                .content(asJsonString(requestFriends))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"));
    }
    @Test
    public void testGetFriendsList() throws Exception {
        User user1 = new User("newmooncsu@gmail.com");
        RequestFriendsList requestFriendsList = new RequestFriendsList(user1.getEmail());

        List<String> listEmail = new ArrayList<>();
        listEmail.add("newmooncsu@gmail.com");
        listEmail.add("newmooncsu1@gmail.com");
        when(relationshipService.findFriendsList(requestFriendsList.getEmail())).thenReturn(listEmail);

        mockMvc.perform(post("/api/relationship/friends-list")
                .content(asJsonString(requestFriendsList))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.friends[0]",is("newmooncsu@gmail.com")))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));
        verify(relationshipService, times(1)).findFriendsList(requestFriendsList.getEmail());
        verifyNoMoreInteractions(relationshipService);
    }
    @Test
    public void testGetFriendsListFailByEmailWrongFormat() throws Exception {
        User user1 = new User("newmooncsu");
        RequestFriendsList requestFriendsList = new RequestFriendsList(user1.getEmail());

        List<String> listEmail = new ArrayList<>();
        listEmail.add("newmooncsu");
        listEmail.add("newmooncsu1");
        when(relationshipService.findFriendsList(requestFriendsList.getEmail())).thenReturn(listEmail);

        mockMvc.perform(post("/api/relationship/friends-list")
                .content(asJsonString(requestFriendsList))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"));
    }
    @Test
    public void testGetFriendsListFailByEmailNull() throws Exception {
        User user1 = new User("newmooncsu");
        RequestFriendsList requestFriendsList = new RequestFriendsList(user1.getEmail());

        List<String> listEmail = new ArrayList<>();
        listEmail.add(null);
        listEmail.add(null);
        when(relationshipService.findFriendsList(requestFriendsList.getEmail())).thenReturn(listEmail);

        mockMvc.perform(post("/api/relationship/friends-list")
                .content(asJsonString(requestFriendsList))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"));
    }
    @Test
    public void testGetFriendsListFailByEmailEmpty() throws Exception {
        User user1 = new User("newmooncsu");
        RequestFriendsList requestFriendsList = new RequestFriendsList(user1.getEmail());

        List<String> listEmail = new ArrayList<>();
        listEmail.add("");
        listEmail.add("");
        when(relationshipService.findFriendsList(requestFriendsList.getEmail())).thenReturn(listEmail);

        mockMvc.perform(post("/api/relationship/friends-list")
                .content(asJsonString(requestFriendsList))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"));
    }
    @Test
    public void testGetCommonFriendsList() throws Exception{
        List<String> listEmail = new ArrayList<>();
        listEmail.add("newmooncsu@gmail.com");
        listEmail.add("newmooncsu1@gmail.com");
        RequestFriends requestFriends = new RequestFriends(listEmail);

        List<String> lstEmail = new ArrayList<>();
        lstEmail.add("newmooncsu@gmail.com");
        lstEmail.add("newmooncsu1@gmail.com");

        when(relationshipService.findCommonFriendsList(lstEmail)).thenReturn(lstEmail);
        mockMvc.perform(post("/api/relationship/common-friends-list")
                .content(asJsonString(requestFriends))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));
        verify(relationshipService, times(1)).findCommonFriendsList(lstEmail);
        verifyNoMoreInteractions(relationshipService);
    }
    @Test
    public void testGetCommonFriendsListFailByEmailWrongFormat() throws Exception{
        List<String> listEmail = new ArrayList<>();
        listEmail.add("newmooncsu");
        listEmail.add("newmooncsu1");
        RequestFriends requestFriends = new RequestFriends(listEmail);

        List<String> lstEmail = new ArrayList<>();
        lstEmail.add("newmooncsu");
        lstEmail.add("newmooncsu1");

        when(relationshipService.findCommonFriendsList(lstEmail)).thenReturn(lstEmail);
        mockMvc.perform(post("/api/relationship/common-friends-list")
                .content(asJsonString(requestFriends))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"));
    }
    @Test
    public void testGetCommonFriendsListFailByEmailNull() throws Exception{
        List<String> listEmail = new ArrayList<>();
        listEmail.add(null);
        listEmail.add(null);
        RequestFriends requestFriends = new RequestFriends(listEmail);

        List<String> lstEmail = new ArrayList<>();
        lstEmail.add("newmooncsu");
        lstEmail.add("newmooncsu1");

        when(relationshipService.findCommonFriendsList(lstEmail)).thenReturn(lstEmail);
        mockMvc.perform(post("/api/relationship/common-friends-list")
                .content(asJsonString(requestFriends))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"));
    }
    @Test
    public void testGetCommonFriendsListFailByEmailEmpty() throws Exception{
        List<String> listEmail = new ArrayList<>();
        listEmail.add("");
        listEmail.add("");
        RequestFriends requestFriends = new RequestFriends(listEmail);

        List<String> lstEmail = new ArrayList<>();
        lstEmail.add("newmooncsu");
        lstEmail.add("newmooncsu1");

        when(relationshipService.findCommonFriendsList(lstEmail)).thenReturn(lstEmail);
        mockMvc.perform(post("/api/relationship/common-friends-list")
                .content(asJsonString(requestFriends))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"));
    }
    @Test
    public void testBeSubscriber() throws  Exception{
        Relationship relationship =new Relationship();

        User user1 = new User("newmooncsu@gmail.com");
        User user2 = new User("newmooncsu1@gmail.com");

        RequestSubcriber requestSubcriber = new RequestSubcriber(user1.getEmail(), user2.getEmail());

        when(relationshipService.beSubscriber(user1.getEmail(), user2.getEmail())).thenReturn(relationship);

        mockMvc.perform(post("/api/relationship/be-subscriber")
                .content(asJsonString(requestSubcriber))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));
        verify(relationshipService, times(1)).beSubscriber(user1.getEmail(), user2.getEmail());
        verifyNoMoreInteractions(relationshipService);
    }

    @Test
    public void testBeSubscriberFailByEmailWrongFormat() throws  Exception{
        Relationship relationship =new Relationship();

        User user1 = new User("newmooncsu");
        User user2 = new User("newmooncsu1");

        RequestSubcriber requestSubcriber = new RequestSubcriber(user1.getEmail(), user2.getEmail());

        when(relationshipService.beSubscriber(user1.getEmail(), user2.getEmail())).thenReturn(relationship);

        mockMvc.perform(post("/api/relationship/be-subscriber")
                .content(asJsonString(requestSubcriber))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"));
    }
    @Test
    public void testBeSubscriberFailByEmailNull() throws  Exception{
        Relationship relationship =new Relationship();

        User user1 = new User("");
        User user2 = new User("");

        RequestSubcriber requestSubcriber = new RequestSubcriber(null, null);

        when(relationshipService.beSubscriber(user1.getEmail(), user2.getEmail())).thenReturn(relationship);

        mockMvc.perform(post("/api/relationship/be-subscriber")
                .content(asJsonString(requestSubcriber))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"));
    }
    @Test
    public void testBeSubscriberFailByEmailEmpty() throws  Exception{
        Relationship relationship =new Relationship();

        User user1 = new User("");
        User user2 = new User("");

        RequestSubcriber requestSubcriber = new RequestSubcriber(user1.getEmail(), user2.getEmail());

        when(relationshipService.beSubscriber(user1.getEmail(), user2.getEmail())).thenReturn(relationship);

        mockMvc.perform(post("/api/relationship/be-subscriber")
                .content(asJsonString(requestSubcriber))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"));
    }
    @Test
    public void testToBlock() throws Exception{
        Relationship relationship =new Relationship();
        User user1 = new User("newmooncsu@gmail.com");
        User user2 = new User("newmooncsu1@gmail.com");
        RequestSubcriber requestSubcriber = new RequestSubcriber(user1.getEmail(), user2.getEmail());

        when(relationshipService.toBlock(user1.getEmail(), user2.getEmail())).thenReturn(relationship);
        mockMvc.perform(post("/api/relationship/to-block")
                .content(asJsonString(requestSubcriber))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));
        verify(relationshipService, times(1)).toBlock(user1.getEmail(), user2.getEmail());
        verifyNoMoreInteractions(relationshipService);
    }
    @Test
    public void testToBlockFailByEmailWrongFormat() throws Exception{
        Relationship relationship =new Relationship();
        User user1 = new User("newmooncsu");
        User user2 = new User("newmooncsu1");
        RequestSubcriber requestSubcriber = new RequestSubcriber(user1.getEmail(), user2.getEmail());

        when(relationshipService.toBlock(user1.getEmail(), user2.getEmail())).thenReturn(relationship);
        mockMvc.perform(post("/api/relationship/to-block")
                .content(asJsonString(requestSubcriber))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"));
    }
    @Test
    public void testToBlockFailByEmailNull() throws Exception{
        Relationship relationship =new Relationship();
        User user1 = new User("null");
        User user2 = new User("null");
        RequestSubcriber requestSubcriber = new RequestSubcriber(null, null);

        when(relationshipService.toBlock(user1.getEmail(), user2.getEmail())).thenReturn(relationship);
        mockMvc.perform(post("/api/relationship/to-block")
                .content(asJsonString(requestSubcriber))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"));
    }
    @Test
    public void testToBlockFailByEmailEmpty() throws Exception{
        Relationship relationship =new Relationship();
        User user1 = new User("");
        User user2 = new User("");
        RequestSubcriber requestSubcriber = new RequestSubcriber(user1.getEmail(), user2.getEmail());

        when(relationshipService.toBlock(user1.getEmail(), user2.getEmail())).thenReturn(relationship);
        mockMvc.perform(post("/api/relationship/to-block")
                .content(asJsonString(requestSubcriber))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"));
    }
    @Test
    public void testReceiveUpdate() throws Exception{
        User user = new User("newmooncsu@gmail.com");
        String text = "newmooncs1u@gmail.com";
        List<String> listReceiveUpload = new ArrayList<>();
        listReceiveUpload.add("newmooncsu@gmail.com");
        RequestReciveUpdate requestReciveUpdate = new RequestReciveUpdate(user.getEmail(),text);

        when(relationshipService.findReceiveUpdateList(user.getEmail(),text)).thenReturn(listReceiveUpload);
        MvcResult result = mockMvc.perform(post("/api/relationship/receive-update")
                .content(asJsonString(requestReciveUpdate))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                //.andExpect(jsonPath("$.recipients", is(""+"[len]"+"")))
                .andExpect(jsonPath("$.*", Matchers.hasSize(2))).andReturn();
        String content = result.getResponse().getContentAsString();

        verify(relationshipService, times(1)).findReceiveUpdateList(user.getEmail(),text);
        verifyNoMoreInteractions(relationshipService);
    }
    @Test
    public void testReceiveUpdateFailByEmailWrongFormat() throws Exception{
        User user = new User("newmooncsu");
        String text = "newmooncs1u";
        List<String> listReceiveUpload = new ArrayList<>();
        listReceiveUpload.add("newmooncsu");
        RequestReciveUpdate requestReciveUpdate = new RequestReciveUpdate(user.getEmail(),text);

        when(relationshipService.findReceiveUpdateList(user.getEmail(),text)).thenReturn(listReceiveUpload);
        MvcResult result = mockMvc.perform(post("/api/relationship/receive-update")
                .content(asJsonString(requestReciveUpdate))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json")).andReturn();
        String content = result.getResponse().getContentAsString();
    }
    @Test
    public void testReceiveUpdateFailByEmailNull() throws Exception{
        User user = new User("");
        String text = "newmooncs1u";
        List<String> listReceiveUpload = new ArrayList<>();
        listReceiveUpload.add(null);
        RequestReciveUpdate requestReciveUpdate = new RequestReciveUpdate(null,text);

        when(relationshipService.findReceiveUpdateList(user.getEmail(),text)).thenReturn(listReceiveUpload);
        MvcResult result = mockMvc.perform(post("/api/relationship/receive-update")
                .content(asJsonString(requestReciveUpdate))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json")).andReturn();
        String content = result.getResponse().getContentAsString();
    }
    @Test
    public void testReceiveUpdateFailByEmailEmpty() throws Exception{
        User user = new User("");
        String text = "newmooncs1u";
        List<String> listReceiveUpload = new ArrayList<>();
        listReceiveUpload.add("");
        RequestReciveUpdate requestReciveUpdate = new RequestReciveUpdate(user.getEmail(),text);

        when(relationshipService.findReceiveUpdateList(user.getEmail(),text)).thenReturn(listReceiveUpload);
        MvcResult result = mockMvc.perform(post("/api/relationship/receive-update")
                .content(asJsonString(requestReciveUpdate))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json")).andReturn();
        String content = result.getResponse().getContentAsString();
    }
}
