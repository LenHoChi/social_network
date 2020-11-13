package com.example.service;

import com.example.dto.RelationshipDTO;
import com.example.exception.RelationshipException;
import com.example.model.Relationship;
import com.example.model.RelationshipPK;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface RelationshipService {
    Optional<RelationshipDTO> getRelationshipById(RelationshipPK relationshipPK);

    List<RelationshipDTO> getAllRelationships();

    Boolean beFriends(String userEmail, String friendEmail) throws RelationshipException;

    List<String> getFriendsList(String email);

    List<String> getCommonFriendsList(List<String> emailList) throws RelationshipException;

    Relationship beSubciber(String email_requestor, String email_target) throws RelationshipException;

    Relationship toBlock(String email_requestor, String email_target) throws RelationshipException;

    List<String> getReceiveUpdateList(String email);
}
