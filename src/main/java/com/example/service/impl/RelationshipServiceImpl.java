package com.example.service.impl;

import com.example.dto.RelationshipDTO;
import com.example.exception.RelationshipException;
import com.example.exception.ResouceNotFoundException;
import com.example.model.Relationship;
import com.example.model.RelationshipPK;
import com.example.repository.RelationshipRepository;
import com.example.repository.UserRepository;
import com.example.service.RelationshipService;
import com.example.utils.convert.RelationshipConvert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RelationshipServiceImpl implements RelationshipService {
    @Autowired
    RelationshipRepository relationshipRepository;
    @Autowired
    UserRepository userRepository;
    RelationshipConvert relationshipConvert;
    @Override
    public Optional<RelationshipDTO> getRelationshipById(RelationshipPK relationshipPK) {
        return Optional.ofNullable(relationshipConvert.modelToDTO(relationshipRepository.findById(relationshipPK).get()));
    }

    @Override
    public List<RelationshipDTO> getAllRelationships() {
        return relationshipConvert.listModelToListDTO(relationshipRepository.findAll());
    }

    @Override
    public Boolean beFriends(String userEmail, String friendEmail) throws RelationshipException {
        if (userEmail.equalsIgnoreCase(friendEmail)) {
            throw new RelationshipException("two emails are same");
        }

        RelationshipPK relationshipPK = new RelationshipPK(userEmail, friendEmail);
        Relationship relationship = getRelationship(relationshipPK);

        RelationshipPK relationshipPK1 = new RelationshipPK(friendEmail, userEmail);
        Relationship relationship1 = getRelationship(relationshipPK1);

        relationshipRepository.save(relationship);
        relationshipRepository.save(relationship1);
        return true;
    }

    public Relationship getRelationship(RelationshipPK relationshipPK) {
        Relationship relationship = null;
        if (relationshipRepository.existsById(relationshipPK)) {
            Optional<Relationship> relationship1 = relationshipRepository.findById(relationshipPK);
            if (relationship1.isPresent()) {
                relationship = relationship1.get();
                if (!relationship.getIsBlock()) {
                    relationship.setAreFriends(true);
                } else {
                    return null;
                }
            }
        } else {
            relationship = new Relationship(relationshipPK, true, false, false);
        }
        return relationship;
    }

    @Override
    public List<String> getFriendsList(String email) {
        List<String> lst = new ArrayList<>();
        if (userRepository.existsById(email)) {
            lst = relationshipRepository.getFriendList(email);
        } else {
            throw new ResouceNotFoundException("Not found any email match with input");
        }
        return lst;
    }

    @Override
    public List<String> getCommonFriendsList(List<String> emailList) throws RelationshipException {
        List<String> lst = new ArrayList<>();
        if (emailList.get(0).equalsIgnoreCase(emailList.get(1))) {
            throw new RelationshipException("two emails are same");
        }
        if (userRepository.existsById(emailList.get(0)) && userRepository.existsById(emailList.get(1))) {
            lst = relationshipRepository.getCommonFriendList(emailList);
        } else {
            //throw new RelationshipException("emails do not match");
            throw new ResouceNotFoundException("email do not match");
        }
        return lst;
    }

    @Override
    public boolean beSubciber(String email_requestor, String email_target) throws RelationshipException {
        if (email_requestor.equalsIgnoreCase(email_target)) {
            throw new RelationshipException("two emails are same");
        }
        if (userRepository.existsById(email_requestor) && userRepository.existsById(email_target)) {
            RelationshipPK relationshipPK = new RelationshipPK(email_requestor, email_target);
            Relationship relationship = new Relationship();
            if (relationshipRepository.existsById(relationshipPK)) {
                Optional<Relationship> relationship1 = relationshipRepository.findById(relationshipPK);
                relationship = relationship1.get();
                relationship.setIsSubcriber(true);
            } else {
                relationship = new Relationship(relationshipPK, false, true, false);
            }
            relationshipRepository.save(relationship);
        } else {
            throw new ResouceNotFoundException("email do not match");
        }
        return true;
    }

    @Override
    public boolean toBlock(String email_requestor, String email_target) throws RelationshipException {
        if (email_requestor.equalsIgnoreCase(email_target)) {
            throw new RelationshipException("two emails are same");
        }
        if (userRepository.existsById(email_requestor) && userRepository.existsById(email_target)) {
            RelationshipPK relationshipPK = new RelationshipPK(email_requestor, email_target);
            Relationship relationship = new Relationship();
            if (relationshipRepository.existsById(relationshipPK)) {
                Optional<Relationship> relationship1 = relationshipRepository.findById(relationshipPK);
                relationship = relationship1.get();
                relationship.setIsSubcriber(false);
                if (!relationship.getAreFriends()) {
                    relationship.setIsBlock(true);
                }
            } else {
                relationship = new Relationship(relationshipPK, false, false, true);
            }
            relationshipRepository.save(relationship);
        } else {
            throw new ResouceNotFoundException("not found email matched");
        }
        return true;
    }

    @Override
    public List<String> getReceiveUpdateList(String email) {
        List<String> lst = new ArrayList<>();
        if (userRepository.existsById(email)) {
            lst = relationshipRepository.getReceiveUpdatesList(email);
        } else {
            throw new ResouceNotFoundException("not found any email matched");
        }
        return lst;
    }
}
