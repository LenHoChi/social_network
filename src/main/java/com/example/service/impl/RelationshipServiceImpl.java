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
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
@Transactional(rollbackFor = Exception.class)
@Service
public class RelationshipServiceImpl implements RelationshipService {
    @Autowired
    RelationshipRepository relationshipRepository;
    @Autowired
    UserRepository userRepository;
    @Override
    public Optional<RelationshipDTO> findRelationshipById(RelationshipPK relationshipPK) {
        return Optional.ofNullable(RelationshipConvert.modelToDTO(relationshipRepository.findById(relationshipPK).get()));
    }

    @Override
    public List<RelationshipDTO> findAllRelationships() {
        return RelationshipConvert.listModelToListDTO(relationshipRepository.findAll());
    }

    @Override
    public Boolean beFriends(String userEmail, String friendEmail) throws Exception {
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

    public Relationship getRelationship(RelationshipPK relationshipPK) throws Exception {
        Relationship relationship = null;
        Optional<Relationship> relationshipRoot = relationshipRepository.findById(relationshipPK);
        if (relationshipRoot.isPresent()) {
            relationship = relationshipRoot.get();
            if (!relationship.getIsBlock()) {
                relationship.setAreFriends(true);
            } else {
                throw new Exception("Error");
            }
        } else {
            relationship = new Relationship(relationshipPK, true, false, false);
        }
        return relationship;
    }

    @Override
    public List<String> findFriendsList(String email) {
        List<String> lstFriendList = new ArrayList<>();
        if (userRepository.existsById(email)) {
            lstFriendList = relationshipRepository.getFriendList(email);
        } else {
            throw new ResouceNotFoundException("Not found any email match with input");
        }
        return lstFriendList;
    }

    @Override
    public List<String> findCommonFriendsList(List<String> emailList) throws RelationshipException {
        List<String> lstCommonFriendList = new ArrayList<>();
        if (emailList.get(0).equalsIgnoreCase(emailList.get(1))) {
            throw new RelationshipException("two emails are same");
        }
        if (userRepository.existsById(emailList.get(0)) && userRepository.existsById(emailList.get(1))) {
            //lstCommonFriendList = relationshipRepository.getCommonFriendList(emailList);
            lstCommonFriendList = this.getCommonFriendList(emailList);
        } else {
            //throw new RelationshipException("emails do not match");
            throw new ResouceNotFoundException("email do not match");
        }
        return lstCommonFriendList;
    }

    public List<String> getCommonFriendList(List<String> emailList) {
        List<String> lstEmailCommon = null;
        List<String> lstEmail1 = relationshipRepository.getFriendList(emailList.get(0));
        List<String> lstEmail2 = relationshipRepository.getFriendList(emailList.get(1));
        lstEmail1.retainAll(lstEmail2);
        lstEmailCommon = lstEmail1;
        return lstEmailCommon;
    }

    @Override
    public Relationship beSubscriber(String email_requestor, String email_target) throws RelationshipException {
        Relationship relationship = null;
        if (email_requestor.equalsIgnoreCase(email_target)) {
            throw new RelationshipException("two emails are same");
        }
        if (userRepository.existsById(email_requestor) && userRepository.existsById(email_target)) {
            RelationshipPK relationshipPK = new RelationshipPK(email_requestor, email_target);
            Optional<Relationship> relationshipRoot = relationshipRepository.findById(relationshipPK);
            if (relationshipRoot.isPresent()) {
                relationship = relationshipRoot.get();
                relationship.setIsSubscriber(true);
            } else {
                relationship = new Relationship(relationshipPK, false, true, false);
            }
            relationshipRepository.save(relationship);
        } else {
            throw new ResouceNotFoundException("email do not match");
        }
        return relationship;
    }

    @Override
    public Relationship toBlock(String email_requestor, String email_target) throws RelationshipException {
        Relationship relationship = null;
        if (email_requestor.equalsIgnoreCase(email_target)) {
            throw new RelationshipException("two emails are same");
        }
        if (userRepository.existsById(email_requestor) && userRepository.existsById(email_target)) {
            RelationshipPK relationshipPK = new RelationshipPK(email_requestor, email_target);
            Optional<Relationship> relationshipRoot = relationshipRepository.findById(relationshipPK);
            if (relationshipRoot.isPresent()) {
                relationship = relationshipRoot.get();
                relationship.setIsSubscriber(false);
                if (!relationship.getAreFriends()) {
                    relationship.setIsBlock(true);
                } else {
                    return relationship;
                }
            } else {
                relationship = new Relationship(relationshipPK, false, false, true);
            }
            relationshipRepository.save(relationship);
        } else {
            throw new ResouceNotFoundException("not found email matched");
        }
        return relationship;
    }

    @Override
    public List<String> findReceiveUpdateList(String email, String text) {
        List<String> lstReceiveUpdate = new ArrayList<>();
        List<String> emailRelationship = splitString(text);
        if (userRepository.existsById(email)) {
            lstReceiveUpdate = relationshipRepository.getReceiveUpdatesList(email);
        } else {
            throw new ResouceNotFoundException("not found any email matched");
        }
        for(int i=0;i<emailRelationship.size();i++){
            lstReceiveUpdate.add(emailRelationship.get(i));
        }
        return lstReceiveUpdate;
    }

    //'regard hochilen@gmail.com ok im fine thanks'
    public List<String> splitString(String text) {
        String[] arrRoot = text.split(" ");
        List<String> lstEmail = new ArrayList<>();
        for (int i = 0; i < arrRoot.length; i++) {
            if (arrRoot[i].contains("@")&&checkMail(arrRoot[i])) {
                lstEmail.add(arrRoot[i]);
            }
        }
        return lstEmail;
    }
    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    public boolean checkMail(final String hex) {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(hex);
        return matcher.matches();
    }
}
