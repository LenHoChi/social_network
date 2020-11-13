package com.example.serviceTest;

import com.example.TestRepositoryConfig;
import com.example.dto.RelationshipDTO;
import com.example.dto.UserDTO;
import com.example.model.Relationship;
import com.example.model.RelationshipPK;
import com.example.model.User;
import com.example.repository.RelationshipRepository;
import com.example.repository.UserRepository;
import com.example.service.impl.RelationshipServiceImpl;
import com.example.utils.convert.RelationshipConvert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
//@ContextConfiguration(classes = TestRepositoryConfig.class)
@AutoConfigureMockMvc(addFilters = false)
public class RelationshipServiseTest {
    @MockBean
    private RelationshipRepository relationshipRepository;
    @MockBean
    private UserRepository userRepository;
    @Autowired
    private RelationshipServiceImpl relationshipServiceImpl;

    private RelationshipConvert relationshipConvert;

    @Test
    public void testGetRelationships() throws Exception {
        RelationshipPK relationshipPK = new RelationshipPK("len1", "len10");
        Relationship relationship = new Relationship(relationshipPK, true, false, false);

        RelationshipPK relationshipPK1 = new RelationshipPK("len2", "len10");
        Relationship relationship1 = new Relationship(relationshipPK, true, false, false);

        List<Relationship> relationshipList = Arrays.asList(relationship, relationship1);
        when(relationshipRepository.findAll()).thenReturn(relationshipList);
        List<RelationshipDTO> relationshipDTOList = relationshipServiceImpl.getAllRelationships();
        assertEquals(relationshipList.size(), relationshipDTOList.size());
    }

    @Test
    public void testGetUserById() throws Exception {
        RelationshipPK relationshipPK = new RelationshipPK("len1", "len10");
        Relationship relationship = new Relationship(relationshipPK, true, false, false);
        when(relationshipRepository.findById(relationshipPK)).thenReturn(Optional.of(relationship));
        Optional<RelationshipDTO> relationshipDTO = relationshipServiceImpl.getRelationshipById(relationshipPK);
        assertEquals(relationship.getRelationshipPK().getUserEmail(), relationshipDTO.get().getRelationshipPK().getUserEmail());
    }

    @Test
    public void testBeFriends() throws Exception {
        RelationshipPK relationshipPK = new RelationshipPK("len1", "len10");
        Relationship relationship = new Relationship(relationshipPK, true, false, false);

        User user1 = new User("len1");
        User user2 = new User("len10");

        when(relationshipRepository.save(Mockito.any(Relationship.class))).thenReturn(relationship);
        Boolean result = relationshipServiceImpl.beFriends(user1.getEmail(), user2.getEmail());
        assertTrue(result);
        //assertFalse(result);
    }

    @Test
    public void testGetFriendsList() throws Exception {
        List<String> lst = new ArrayList<>();
        lst.add("len1");

        User user1 = new User("len1");

        when(relationshipRepository.getFriendList(user1.getEmail())).thenReturn(lst);

        when(userRepository.existsById(user1.getEmail())).thenReturn(true);
        List<String> lst1 = relationshipServiceImpl.getFriendsList(user1.getEmail());

        assertEquals(lst.size(), lst1.size());
    }

    @Test
    public void testGetCommonFriendsList() throws Exception {
        List<String> lst = new ArrayList<>();
        lst.add("len10");
        lst.add("len1");

        when(relationshipRepository.getCommonFriendList(lst)).thenReturn(lst);
        when(userRepository.existsById(Mockito.anyString())).thenReturn(true);
        List<String> lst1 = relationshipServiceImpl.getCommonFriendsList(lst);

        assertEquals(lst.size(), lst1.size());
    }

    @Test
    public void testBeSubciber() throws Exception {
        RelationshipPK relationshipPK = new RelationshipPK("len1", "len10");
        Relationship relationship = new Relationship(relationshipPK, true, true, false);

        User user1 = new User("len1");
        User user2 = new User("len10");

        when(userRepository.existsById(Mockito.any(String.class))).thenReturn(true);
        when(relationshipRepository.save(Mockito.any(Relationship.class))).thenReturn(relationship);
        Boolean result = relationshipServiceImpl.beSubciber(user1.getEmail(), user2.getEmail());
        assertTrue(result);
    }

    @Test
    public void testToBlock() throws Exception {
        RelationshipPK relationshipPK = new RelationshipPK("len1", "len10");
        Relationship relationship = new Relationship(relationshipPK, true, true, false);

        User user1 = new User("len1");
        User user2 = new User("len10");

        when(userRepository.existsById(Mockito.any(String.class))).thenReturn(true);
        when(relationshipRepository.save(Mockito.any(Relationship.class))).thenReturn(relationship);
        Boolean result = relationshipServiceImpl.toBlock(user1.getEmail(), user2.getEmail());
        assertTrue(result);
    }

    @Test
    public void testGetReceiveUpdateList() throws Exception {
        List<String> lst = new ArrayList<>();
        lst.add("len10");
        lst.add("len1");

        User user1 = new User("len1");

        when(relationshipRepository.getReceiveUpdatesList(user1.getEmail())).thenReturn(lst);
        when(userRepository.existsById(user1.getEmail())).thenReturn(true);
        List<String> lst1 = relationshipServiceImpl.getReceiveUpdateList(user1.getEmail());

        assertEquals(lst.size(), lst1.size());
    }
}
