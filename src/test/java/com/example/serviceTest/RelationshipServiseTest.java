package com.example.serviceTest;

import com.example.TestRepositoryConfig;
import com.example.dto.RelationshipDTO;
import com.example.dto.UserDTO;
import com.example.exception.RelationshipException;
import com.example.exception.ResouceNotFoundException;
import com.example.model.Relationship;
import com.example.model.RelationshipPK;
import com.example.model.User;
import com.example.repository.RelationshipRepository;
import com.example.repository.UserRepository;
import com.example.service.impl.RelationshipServiceImpl;
import com.example.utils.convert.RelationshipConvert;
import net.bytebuddy.pool.TypePool;
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
    //test for be friend
    //tc1 happy case
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
    //tc2
    @Test
    public void testBeFriendsSameEmail() throws Exception {
        RelationshipPK relationshipPK = new RelationshipPK("len1", "len10");
        Relationship relationship = new Relationship(relationshipPK, true, false, false);

        User user1 = new User("len1");
        User user2 = new User("len1");

        when(relationshipRepository.save(Mockito.any(Relationship.class))).thenReturn(relationship);
        Throwable exception = assertThrows(RelationshipException.class, () -> relationshipServiceImpl.beFriends(user1.getEmail(), user2.getEmail()));
        assertEquals("two emails are same", exception.getMessage());

    }
    //test for getRelationship
    //tc1
    @Test
    public void testGetRelationshipFalseExists() throws Exception{
        RelationshipPK relationshipPK = new RelationshipPK("newmooncsu@gmail.com", "newmooncsu2@gmail.com");

        when(relationshipRepository.existsById(relationshipPK)).thenReturn(false);
        Relationship relationship = relationshipServiceImpl.getRelationship(relationshipPK);
        assertTrue(relationship.getAreFriends());
        assertFalse(relationship.getIsBlock());
        assertFalse(relationship.getIsSubcriber());
        assertEquals(relationship.getRelationshipPK(), relationshipPK);
    }
    //tc2 th ispresent == empty()
    //tc3 isblock == true
    @Test
    public void testGetRelationshipTrueExists() throws Exception {
        RelationshipPK relationshipPK = new RelationshipPK("newmooncsu@gmail.com", "newmooncsu2@gmail.com");

        Relationship relationship = new Relationship(relationshipPK, false, false, true);

        when(relationshipRepository.existsById(relationshipPK)).thenReturn(true);
        when(relationshipRepository.findById(relationshipPK)).thenReturn(Optional.of(relationship));

        Relationship relationship1 = relationshipServiceImpl.getRelationship(relationshipPK);
        assertNull(relationship1);
    }
    //tc4 isblock == false
    @Test
    public void testGetRelationshipTrueExists2() throws Exception {
        RelationshipPK relationshipPK = new RelationshipPK("newmooncsu@gmail.com", "newmooncsu2@gmail.com");

        Relationship relationship = new Relationship(relationshipPK, false, false, false);

        when(relationshipRepository.existsById(relationshipPK)).thenReturn(true);
        when(relationshipRepository.findById(relationshipPK)).thenReturn(Optional.of(relationship));

        Relationship relationship1 = relationshipServiceImpl.getRelationship(relationshipPK);
        assertTrue(relationship1.getAreFriends());
        assertFalse(relationship1.getIsBlock());
        assertFalse(relationship1.getIsSubcriber());
        assertEquals(relationship1.getRelationshipPK(), relationshipPK);
    }
    //test for getFriendslist
    //tc1 happy case
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
    //tc2 unhappy case
    @Test
    public void testGetFriendsListError() throws Exception{
        List<String> lst = new ArrayList<>();
        lst.add("len1");

        User user1 = new User("len1");

        when(relationshipRepository.getFriendList(user1.getEmail())).thenReturn(lst);

        when(userRepository.existsById(user1.getEmail())).thenReturn(false);
        Throwable exception = assertThrows(ResouceNotFoundException.class, () -> relationshipServiceImpl.getFriendsList(user1.getEmail()));
        assertEquals("Not found any email match with input", exception.getMessage());
    }
    //test for getCommonFriendsList
    //tc1 -- happy case
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
    //tc2 -- unhappy ending
    @Test
    public void testGetCommonFriendsListErrorEmailSame() throws Exception {
        List<String> lst = new ArrayList<>();
        lst.add("len10");
        lst.add("len10");

        when(relationshipRepository.getCommonFriendList(lst)).thenReturn(lst);
        when(userRepository.existsById(Mockito.anyString())).thenReturn(true);
        Throwable exception = assertThrows(RelationshipException.class, () -> relationshipServiceImpl.getCommonFriendsList(lst));
        assertEquals("two emails are same", exception.getMessage());
    }
    //tc3 -- unhappy ending 2
    @Test
    public void testGetCommonFriendsListErrorEmailMatch() throws Exception {
        List<String> lst = new ArrayList<>();
        lst.add("len1");
        lst.add("len10");

        when(relationshipRepository.getCommonFriendList(lst)).thenReturn(lst);
        when(userRepository.existsById(Mockito.anyString())).thenReturn(false);
        Throwable exception = assertThrows(ResouceNotFoundException.class, () -> relationshipServiceImpl.getCommonFriendsList(lst));
        assertEquals("email do not match", exception.getMessage());
    }
    //test for besubcriber
    //tc1 - same email
    @Test
    public void testBeSubcriberSameEmail() throws Exception{
        RelationshipPK relationshipPK = new RelationshipPK("len1", "len10");
        Relationship relationship = new Relationship(relationshipPK, true, true, false);

        User user1 = new User("newmooncsu@gmail.com");
        User user2 = new User("newmooncsu@gmail.com");

        when(userRepository.existsById(Mockito.any(String.class))).thenReturn(true);
        when(relationshipRepository.save(Mockito.any(Relationship.class))).thenReturn(relationship);
        Throwable exception = assertThrows(RelationshipException.class, () -> relationshipServiceImpl.beSubciber(user1.getEmail(), user2.getEmail()));
        assertEquals("two emails are same", exception.getMessage());
    }
    //tc2 -- not match
    @Test
    public void testBeSubcriberNotMatch() throws Exception{
        RelationshipPK relationshipPK = new RelationshipPK("len1", "len10");
        Relationship relationship = new Relationship(relationshipPK, true, true, false);

        User user1 = new User("newmooncsu@gmail.com");
        User user2 = new User("newmooncsu1@gmail.com");

        when(userRepository.existsById(Mockito.any(String.class))).thenReturn(false);
        when(relationshipRepository.save(Mockito.any(Relationship.class))).thenReturn(relationship);
        Throwable exception = assertThrows(ResouceNotFoundException.class, () -> relationshipServiceImpl.beSubciber(user1.getEmail(), user2.getEmail()));
        assertEquals("email do not match", exception.getMessage());
    }
    //tc3 -- not friends
    @Test
    public void testBeSubciberNotFriends() throws Exception {
        RelationshipPK relationshipPK = new RelationshipPK("len1", "len10");
     //   Relationship relationship = new Relationship(relationshipPK, true, false, false);

        User user1 = new User("len1");
        User user2 = new User("len10");

        when(userRepository.existsById(Mockito.any(String.class))).thenReturn(true);
        when(relationshipRepository.existsById(relationshipPK)).thenReturn(false);
        //when(relationshipRepository.save(Mockito.any(Relationship.class))).thenReturn(relationship);
        Relationship result = relationshipServiceImpl.beSubciber(user1.getEmail(), user2.getEmail());
        assertTrue(result.getIsSubcriber());
        assertFalse(result.getAreFriends());
    }
    //tc4 -- were friends
    @Test
    public void testBeSubciberWereFriends() throws Exception {
        RelationshipPK relationshipPK = new RelationshipPK("len1", "len10");
        Relationship relationship = new Relationship(relationshipPK, true, false, false);

        User user1 = new User("len1");
        User user2 = new User("len10");

        when(userRepository.existsById(Mockito.any(String.class))).thenReturn(true);
        when(relationshipRepository.existsById(relationshipPK)).thenReturn(true);
        when(relationshipRepository.findById(relationshipPK)).thenReturn(Optional.of(relationship));
//        when(relationshipRepository.save(Mockito.any(Relationship.class))).thenReturn(relationship);
        Relationship result = relationshipServiceImpl.beSubciber(user1.getEmail(), user2.getEmail());
        assertTrue(result.getIsSubcriber());
    }
    //test for block
    //tc1
    @Test
    public void testToBlockErrorSame() throws Exception {
        RelationshipPK relationshipPK = new RelationshipPK("len1", "len10");
        Relationship relationship = new Relationship(relationshipPK, true, true, false);

        User user1 = new User("len10");
        User user2 = new User("len10");

        when(userRepository.existsById(Mockito.any(String.class))).thenReturn(true);
        //when(relationshipRepository.save(Mockito.any(Relationship.class))).thenReturn(relationship);
        Throwable exception = assertThrows(RelationshipException.class, () -> relationshipServiceImpl.toBlock(user1.getEmail(), user2.getEmail()));
        assertEquals("two emails are same", exception.getMessage());
    }
    //tc2 -- not match
    @Test
    public void testToBlockNotMatch() throws Exception{
        RelationshipPK relationshipPK = new RelationshipPK("len1", "len10");
        //Relationship relationship = new Relationship(relationshipPK, true, true, false);

        User user1 = new User("newmooncsu@gmail.com");
        User user2 = new User("newmooncsu1@gmail.com");

        when(userRepository.existsById(Mockito.any(String.class))).thenReturn(false);
        //when(relationshipRepository.save(Mockito.any(Relationship.class))).thenReturn(relationship);
        Throwable exception = assertThrows(ResouceNotFoundException.class, () -> relationshipServiceImpl.toBlock(user1.getEmail(), user2.getEmail()));
        assertEquals("not found email matched", exception.getMessage());
    }
    //tc3 -- not relationship
    @Test
    public void testToBlockNotRelationship() throws Exception {
        RelationshipPK relationshipPK = new RelationshipPK("len1", "len10");
        //Relationship relationship = new Relationship(relationshipPK, true, false, false);

        User user1 = new User("len1");
        User user2 = new User("len10");

        when(userRepository.existsById(Mockito.any(String.class))).thenReturn(true);
        when(relationshipRepository.existsById(relationshipPK)).thenReturn(false);
        //when(relationshipRepository.save(Mockito.any(Relationship.class))).thenReturn(relationship);
        Relationship result = relationshipServiceImpl.toBlock(user1.getEmail(), user2.getEmail());
        assertTrue(result.getIsBlock());

    }
 //   tc4 -- have relationship  not friends
    @Test
    public void testToBlockHaveRelationshipNotFriends() throws Exception {
        RelationshipPK relationshipPK = new RelationshipPK("len1", "len10");
        Relationship relationship = new Relationship(relationshipPK, false, false, false);

        User user1 = new User("len1");
        User user2 = new User("len10");

        when(userRepository.existsById(Mockito.any(String.class))).thenReturn(true);
        when(relationshipRepository.existsById(relationshipPK)).thenReturn(true);
        when(relationshipRepository.findById(relationshipPK)).thenReturn(Optional.of(relationship));
//        when(relationshipRepository.save(Mockito.any(Relationship.class))).thenReturn(relationship);
        Relationship result = relationshipServiceImpl.toBlock(user1.getEmail(), user2.getEmail());
        assertTrue(result.getIsBlock());
    }
    //   tc5 -- have relationship  were friends
    @Test
    public void testToBlockHaveRelationshipWereFriends() throws Exception {
        RelationshipPK relationshipPK = new RelationshipPK("len1", "len10");
        Relationship relationship = new Relationship(relationshipPK, true, false, false);

        User user1 = new User("len1");
        User user2 = new User("len10");

        when(userRepository.existsById(Mockito.any(String.class))).thenReturn(true);
        when(relationshipRepository.existsById(relationshipPK)).thenReturn(true);
        when(relationshipRepository.findById(relationshipPK)).thenReturn(Optional.of(relationship));
//        when(relationshipRepository.save(Mockito.any(Relationship.class))).thenReturn(relationship);
        Relationship result = relationshipServiceImpl.toBlock(user1.getEmail(), user2.getEmail());
        assertFalse(result.getIsSubcriber());
    }
    //test for reciuveupdatelist
    //tc1
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
    //tc2
    @Test
    public void testGetReceiveUpdateListError() throws Exception {
        List<String> lst = new ArrayList<>();
        lst.add("len10");
        lst.add("len1");

        User user1 = new User("len1");

        when(relationshipRepository.getReceiveUpdatesList(user1.getEmail())).thenReturn(lst);
        when(userRepository.existsById(user1.getEmail())).thenReturn(false);
        Throwable exception = assertThrows(ResouceNotFoundException.class, () -> relationshipServiceImpl.getReceiveUpdateList(user1.getEmail()));
        assertEquals("not found any email matched", exception.getMessage());
    }
}
