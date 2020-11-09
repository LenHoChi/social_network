package com.example.service;

import com.example.model.Relationship;
import com.example.model.RelationshipPK;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface RelationshipService {
    Optional<Relationship> getRelationshipById(RelationshipPK relationshipPK);
    List<Relationship> getAllRelationships();
    Boolean beFriends(String userEmail, String friendEmail);
    List<String> getFriendList(String email);
    List<String> getCommonFriendList(List<String> emailList);
}
