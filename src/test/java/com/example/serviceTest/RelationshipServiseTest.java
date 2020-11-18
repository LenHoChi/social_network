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
import io.jsonwebtoken.lang.Objects;
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

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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
    private RelationshipServiceImpl relationshipService;

    private RelationshipConvert relationshipConvert;

    //test get all relationships
    @Test
    public void testGetRelationships() throws Exception {
        RelationshipPK relationshipPK = new RelationshipPK("len1", "len10");
        Relationship relationship = new Relationship(relationshipPK, true, false, false);

        RelationshipPK relationshipPK1 = new RelationshipPK("len2", "len10");
        Relationship relationship1 = new Relationship(relationshipPK1, true, false, false);

        List<Relationship> relationshipList = Arrays.asList(relationship, relationship1);
        when(relationshipRepository.findAll()).thenReturn(relationshipList);
        List<RelationshipDTO> relationshipDTOList = relationshipService.findAllRelationships();
        assertEquals(relationshipList.size(), relationshipDTOList.size());
    }
    //test get relationship by id
    @Test
    public void testGetRelationshipById() throws Exception {
        RelationshipPK relationshipPK = new RelationshipPK("len1", "len10");
        Relationship relationship = new Relationship(relationshipPK, true, false, false);
        when(relationshipRepository.findById(relationshipPK)).thenReturn(Optional.of(relationship));
        Optional<RelationshipDTO> relationshipDTO = relationshipService.findRelationshipById(relationshipPK);
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
        Boolean result = relationshipService.beFriends(user1.getEmail(), user2.getEmail());
        assertTrue(result);
        //assertFalse(result);
    }
    //tc2 -- 2 email the same
    @Test
    public void testBeFriendsSameEmail() throws Exception {
        RelationshipPK relationshipPK = new RelationshipPK("len1", "len10");
        Relationship relationship = new Relationship(relationshipPK, true, false, false);

        User user1 = new User("len1");
        User user2 = new User("len1");

        when(relationshipRepository.save(Mockito.any(Relationship.class))).thenReturn(relationship);
        Throwable exception = assertThrows(RelationshipException.class, () -> relationshipService.beFriends(user1.getEmail(), user2.getEmail()));
        assertEquals("two emails are same", exception.getMessage());

    }
    //test for getRelationship
    //tc1 -- exists relationship happy case
    @Test
    public void testGetRelationshipFalseExists() throws Exception{
        RelationshipPK relationshipPK = new RelationshipPK("newmooncsu@gmail.com", "newmooncsu2@gmail.com");
        Relationship relationship = new Relationship(relationshipPK, false, false, false);
        when(relationshipRepository.findById(relationshipPK)).thenReturn(Optional.of(relationship));

        Relationship relationshipResult = relationshipService.getRelationship(relationshipPK);
        assertTrue(relationshipResult.getAreFriends());
        assertFalse(relationshipResult.getIsBlock());
        assertFalse(relationshipResult.getIsSubscriber());
        assertEquals(relationshipResult.getRelationshipPK(), relationshipPK);
    }
    //tc2 th isPresent == empty()
    @Test
    public void testGetRelationshipIsEmpty() throws Exception {
        RelationshipPK relationshipPK = new RelationshipPK("newmooncsu@gmail.com", "newmooncsu2@gmail.com");

        when(relationshipRepository.findById(relationshipPK)).thenReturn(Optional.empty());
        Relationship relationshipResult = relationshipService.getRelationship(relationshipPK);
        assertTrue(relationshipResult.getAreFriends());
        assertFalse(relationshipResult.getIsBlock());
        assertFalse(relationshipResult.getIsSubscriber());
        assertEquals(relationshipResult.getRelationshipPK(), relationshipPK);
//        Relationship relationship1 = relationshipServiceImpl.getRelationship(relationshipPK);
//        Collection collection =(Collection) relationship1;
//        assertTrue(collection.isEmpty());
    }
    //tc3 isBlock == true
    @Test
    public void testGetRelationshipTrueBlock() throws Exception {
        RelationshipPK relationshipPK = new RelationshipPK("newmooncsu@gmail.com", "newmooncsu2@gmail.com");

        Relationship relationship = new Relationship(relationshipPK, false, false, true);

        when(relationshipRepository.findById(relationshipPK)).thenReturn(Optional.of(relationship));
        Throwable exception = assertThrows(Exception.class, () -> relationshipService.getRelationship(relationshipPK));
        assertEquals("Error", exception.getMessage());
//        Relationship relationship1 = relationshipServiceImpl.getRelationship(relationshipPK);
//        assertNull(relationship1);
    }
    //tc4 isBlock == false
    @Test
    public void testGetRelationshipFalseBlock() throws Exception {
        RelationshipPK relationshipPK = new RelationshipPK("newmooncsu@gmail.com", "newmooncsu2@gmail.com");

        Relationship relationship = new Relationship(relationshipPK, false, false, false);

        when(relationshipRepository.existsById(relationshipPK)).thenReturn(true);
        when(relationshipRepository.findById(relationshipPK)).thenReturn(Optional.of(relationship));

        Relationship relationship1 = relationshipService.getRelationship(relationshipPK);
        assertTrue(relationship1.getAreFriends());
        assertFalse(relationship1.getIsBlock());
        assertFalse(relationship1.getIsSubscriber());
        assertEquals(relationship1.getRelationshipPK(), relationshipPK);
    }
    //test for getListFriends
    //tc1 happy case
    @Test
    public void testGetFriendsList() throws Exception {
        List<String> lstFriend = new ArrayList<>();
        lstFriend.add("len1");

        User user = new User("len1");

        when(relationshipRepository.getFriendList(user.getEmail())).thenReturn(lstFriend);

        when(userRepository.existsById(user.getEmail())).thenReturn(true);
        List<String> lstTest = relationshipService.findFriendsList(user.getEmail());

        assertEquals(lstFriend.size(), lstTest.size());
    }
    //tc2 unhappy case
    @Test
    public void testGetFriendsListError() throws Exception{
        List<String> lstFriend = new ArrayList<>();
        lstFriend.add("len1");

        User user = new User("len1");

        when(relationshipRepository.getFriendList(user.getEmail())).thenReturn(lstFriend);

        when(userRepository.existsById(user.getEmail())).thenReturn(false);
        Throwable exception = assertThrows(ResouceNotFoundException.class, () -> relationshipService.findFriendsList(user.getEmail()));
        assertEquals("Not found any email match with input", exception.getMessage());
    }
    //test for getCommonFriendsList
    //tc1 -- happy case
    @Test
    public void testGetCommonFriendsList() throws Exception {
        List<String> lstCommonFriend = new ArrayList<>();
        lstCommonFriend.add("len10");
        lstCommonFriend.add("len1");
        List<String> lstTest = new ArrayList<>();
        when(relationshipRepository.getFriendList(Mockito.anyString())).thenReturn(lstCommonFriend);
        when(userRepository.existsById(Mockito.anyString())).thenReturn(true);
        lstTest = relationshipService.findCommonFriendsList(lstCommonFriend);

        assertEquals(lstCommonFriend.size(), lstTest.size());
    }
    //tc2 -- unhappy ending
    @Test
    public void testGetCommonFriendsListErrorEmailSame() throws Exception {
        List<String> lstCommonFriend = new ArrayList<>();
        lstCommonFriend.add("len10");
        lstCommonFriend.add("len10");

        //when(relationshipRepository.getCommonFriendList(lst)).thenReturn(lst);
        when(relationshipService.getCommonFriendList(lstCommonFriend)).thenReturn(lstCommonFriend);
        when(userRepository.existsById(Mockito.anyString())).thenReturn(true);
        Throwable exception = assertThrows(RelationshipException.class, () -> relationshipService.findCommonFriendsList(lstCommonFriend));
        assertEquals("two emails are same", exception.getMessage());
    }
    //tc3 -- unhappy ending 2
    @Test
    public void testGetCommonFriendsListErrorEmailMatch() throws Exception {
        List<String> lstCommonFriend = new ArrayList<>();
        lstCommonFriend.add("len1");
        lstCommonFriend.add("len10");

        //when(relationshipRepository.getCommonFriendList(lstCommonFriend)).thenReturn(lstCommonFriend);
        when(relationshipService.getCommonFriendList(lstCommonFriend)).thenReturn(lstCommonFriend);
        when(userRepository.existsById(Mockito.anyString())).thenReturn(false);
        Throwable exception = assertThrows(ResouceNotFoundException.class, () -> relationshipService.findCommonFriendsList(lstCommonFriend));
        assertEquals("email do not match", exception.getMessage());
    }
    //test for beSubscriber
    //tc1 - same email
    @Test
    public void testBeSubcriberSameEmail() throws Exception{
        RelationshipPK relationshipPK = new RelationshipPK("len1", "len10");
        Relationship relationship = new Relationship(relationshipPK, true, true, false);

        User user1 = new User("newmooncsu@gmail.com");
        User user2 = new User("newmooncsu@gmail.com");

        when(userRepository.existsById(Mockito.any(String.class))).thenReturn(true);
        Throwable exception = assertThrows(RelationshipException.class, () -> relationshipService.beSubscriber(user1.getEmail(), user2.getEmail()));
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
        Throwable exception = assertThrows(ResouceNotFoundException.class, () -> relationshipService.beSubscriber(user1.getEmail(), user2.getEmail()));
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
        when(relationshipRepository.findById(relationshipPK)).thenReturn(Optional.empty());
        Relationship result = relationshipService.beSubscriber(user1.getEmail(), user2.getEmail());
        assertTrue(result.getIsSubscriber());
        assertFalse(result.getAreFriends());
    }
    //tc4 -- were friends
    @Test
    public void testBeSubscriberWereFriends() throws Exception {
        RelationshipPK relationshipPK = new RelationshipPK("len1", "len10");
        Relationship relationship = new Relationship(relationshipPK, true, false, false);

        User user1 = new User("len1");
        User user2 = new User("len10");

        when(userRepository.existsById(Mockito.any(String.class))).thenReturn(true);
        when(relationshipRepository.findById(relationshipPK)).thenReturn(Optional.of(relationship));
//        when(relationshipRepository.save(Mockito.any(Relationship.class))).thenReturn(relationship);
        Relationship result = relationshipService.beSubscriber(user1.getEmail(), user2.getEmail());
        assertTrue(result.getIsSubscriber());
    }
    //test for block
    //tc1 -- same email
    @Test
    public void testToBlockErrorSame() throws Exception {
        RelationshipPK relationshipPK = new RelationshipPK("len1", "len10");
        Relationship relationship = new Relationship(relationshipPK, true, true, false);

        User user1 = new User("len10");
        User user2 = new User("len10");

        when(userRepository.existsById(Mockito.any(String.class))).thenReturn(true);
        Throwable exception = assertThrows(RelationshipException.class, () -> relationshipService.toBlock(user1.getEmail(), user2.getEmail()));
        assertEquals("two emails are same", exception.getMessage());
    }
    //tc2 -- not exists email
    @Test
    public void testToBlockNotMatch() throws Exception{

        User user1 = new User("newmooncsu@gmail.com");
        User user2 = new User("newmooncsu1@gmail.com");

        when(userRepository.existsById(Mockito.any(String.class))).thenReturn(false);
        Throwable exception = assertThrows(ResouceNotFoundException.class, () -> relationshipService.toBlock(user1.getEmail(), user2.getEmail()));
        assertEquals("not found email matched", exception.getMessage());
    }
    //tc3 -- not relationship
    @Test
    public void testToBlockNotRelationship() throws Exception {
        RelationshipPK relationshipPK = new RelationshipPK("len1", "len10");

        User user1 = new User("len1");
        User user2 = new User("len10");

        when(userRepository.existsById(Mockito.any(String.class))).thenReturn(true);
        when(relationshipRepository.findById(relationshipPK)).thenReturn(Optional.empty());
        Relationship result = relationshipService.toBlock(user1.getEmail(), user2.getEmail());
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
        when(relationshipRepository.findById(relationshipPK)).thenReturn(Optional.of(relationship));
        Relationship result = relationshipService.toBlock(user1.getEmail(), user2.getEmail());
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
        when(relationshipRepository.findById(relationshipPK)).thenReturn(Optional.of(relationship));
        Relationship result = relationshipService.toBlock(user1.getEmail(), user2.getEmail());
        assertFalse(result.getIsSubscriber());
    }
    //test for reciuveupdatelist
    //tc1 -- happycase
    @Test
    public void testGetReceiveUpdateList() throws Exception {
        List<String> lst = new ArrayList<>();
        lst.add("len10");
        lst.add("len1");

        User user1 = new User("len1");

        when(relationshipRepository.getReceiveUpdatesList(user1.getEmail())).thenReturn(lst);
        when(userRepository.existsById(user1.getEmail())).thenReturn(true);
        List<String> lst1 = relationshipService.findReceiveUpdateList(user1.getEmail());

        assertEquals(lst.size(), lst1.size());
    }
    //tc2 -- not found
    @Test
    public void testGetReceiveUpdateListError() throws Exception {
        List<String> lst = new ArrayList<>();
        lst.add("len10");
        lst.add("len1");

        User user1 = new User("len1");

        when(relationshipRepository.getReceiveUpdatesList(user1.getEmail())).thenReturn(lst);
        when(userRepository.existsById(user1.getEmail())).thenReturn(false);
        Throwable exception = assertThrows(ResouceNotFoundException.class, () -> relationshipService.findReceiveUpdateList(user1.getEmail()));
        assertEquals("not found any email matched", exception.getMessage());
    }
}
