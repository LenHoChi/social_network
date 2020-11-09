package com.example.service.impl;

import com.example.model.Relationship;
import com.example.model.RelationshipPK;
import com.example.repository.RelationshipRepository;
import com.example.repository.UserRepository;
import com.example.service.RelationshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class RelationshipServiceImpl implements RelationshipService {
    @Autowired
    RelationshipRepository relationshipRepository;
    @Autowired
    UserRepository userRepository;
    @Override
    public Optional<Relationship> getRelationshipById(RelationshipPK relationshipPK) {
        return relationshipRepository.findById(relationshipPK);
    }

    @Override
    public List<Relationship> getAllRelationships() {
        return relationshipRepository.findAll();
    }

    @Override
    public Boolean beFriends(String userEmail, String friendEmail) {
        RelationshipPK relationshipPK = new RelationshipPK(userEmail,friendEmail);
        Relationship relationship = new Relationship(relationshipPK,true,false);
        relationshipRepository.save(relationship);
        return true;
    }

    @Override
    public List<String> getFriendList(String email) {
        return relationshipRepository.getFriendList(email);
    }

    @Override
    public List<String> getCommonFriendList(List<String> emailList) {
        return relationshipRepository.getCommonFriendList(emailList);
//        return null;
    }

    @Override
    public boolean beSubciber(String email_requestor, String email_target) {
        if(userRepository.existsById(email_requestor)&&userRepository.existsById(email_target)){
            RelationshipPK relationshipPK = new RelationshipPK(email_requestor,email_target);
            Relationship relationship = new Relationship();
            if(relationshipRepository.existsById(relationshipPK)){
                Optional<Relationship> relationship1 = relationshipRepository.findById(relationshipPK);
                relationship = relationship1.get();
                relationship.setIsSubcriber(true);
            }
            else{
                relationship = new Relationship(relationshipPK,false,true);
            }
            relationshipRepository.save(relationship);
            return true;
        }
        return false;
    }
}
