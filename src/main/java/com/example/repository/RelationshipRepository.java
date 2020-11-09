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
    @Query("select r.relationshipPK.friendEmail "
            + "from Relationship r "
            + "where r.relationshipPK.userEmail = :email "
            + "and r.areFriends = true")
    List<String> getFriendList(@Param("email") String email);

    @Query("SELECT r.relationshipPK.friendEmail "
            + "FROM Relationship r "
            + "where r.relationshipPK.userEmail in :email "
            + "and r.areFriends = true "
            + "group by r.relationshipPK.friendEmail "
            + "having count(r.relationshipPK.friendEmail) > 1 ")
    List<String> getCommonFriendList(@Param("email") List<String> email);
}
