package com.example.service;

import com.example.exception.RelationshipException;
import com.example.model.Relationship;
import com.example.model.RelationshipPK;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface RelationshipService {
    Optional<Relationship> getRelationshipById(RelationshipPK relationshipPK);

    List<Relationship> getAllRelationships();

    Boolean beFriends(String userEmail, String friendEmail) throws RelationshipException;

    List<String> getFriendsList(String email);

    List<String> getCommonFriendsList(List<String> emailList) throws RelationshipException;

    boolean beSubciber(String email_requestor, String email_target) throws RelationshipException;

    boolean toBlock(String email_requestor, String email_target) throws RelationshipException;

    List<String> getReceiveUpdateList(String email);
}
