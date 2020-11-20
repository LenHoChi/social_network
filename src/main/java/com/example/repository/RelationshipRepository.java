package com.example.repository;

import com.example.model.Relationship;
import com.example.model.RelationshipPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RelationshipRepository extends JpaRepository<Relationship, RelationshipPK> {
//    @Query("select r.relationshipPK.friendEmail "
//            + "from Relationship r "
//            + "where r.relationshipPK.userEmail = :email "
//            + "and r.areFriends = true ")
//    List<String> getFriendList(@Param("email") String email);

    String findQuery = "select r.friend_Email \n" +
            "from Relationship r \n" +
            "where r.user_Email = :email\n" +
            "and r.areFriends = true \n" +
            "union \n" +
            "select r.user_Email \n" +
            "from Relationship r \n" +
            "where r.friend_Email = :email\n" +
            "and r.areFriends = true ";
    @Query(value = findQuery, nativeQuery = true)
    List<String> findFriendList(@Param("email") String email);

//    @Query("SELECT r.relationshipPK.friendEmail "
//            + "FROM Relationship r "
//            + "where r.relationshipPK.userEmail in :email "
//            + "and r.areFriends = true "
//            + "group by r.relationshipPK.friendEmail "
//            + "having count(r.relationshipPK.friendEmail) > 1 ")
//    List<String> getCommonFriendList(@Param("email") List<String> email);

    @Query("SELECT r.relationshipPK.friendEmail "
            + "FROM Relationship r "
            + "where r.relationshipPK.userEmail = :email "
            + "and (r.areFriends = true "
            + "or r.isSubscriber = true) "
            + "and r.isBlock = false")
    List<String> findReceiveUpdatesList(@Param("email") String email);
}
