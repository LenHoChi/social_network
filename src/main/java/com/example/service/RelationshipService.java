package com.example.service;

import com.example.dto.RelationshipDTO;
import com.example.exception.RelationshipException;
import com.example.model.Relationship;
import com.example.model.RelationshipPK;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface RelationshipService {
    Optional<RelationshipDTO> findRelationshipById(RelationshipPK relationshipPK);

    List<RelationshipDTO> findAllRelationships();

    Boolean beFriends(String userEmail, String friendEmail) throws Exception;

    List<String> findFriendsList(String email);

    List<String> findCommonFriendsList(List<String> emailList) throws RelationshipException;

    Relationship beSubscriber(String email_requestor, String email_target) throws RelationshipException;

    Relationship toBlock(String email_requestor, String email_target) throws RelationshipException;

    List<String> findReceiveUpdateList(String email);
}
