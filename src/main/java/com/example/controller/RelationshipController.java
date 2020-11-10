package com.example.controller;

import com.example.exception.RelationshipException;
import com.example.model.Relationship;
import com.example.model.RelationshipPK;
import com.example.service.RelationshipService;
import com.example.utils.request.RequestFriends;
import com.example.utils.request.RequestFriendsList;
import com.example.utils.request.RequestReciveUpdate;
import com.example.utils.request.RequestSubcriber;
import com.example.utils.response.ResponseFriends;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class RelationshipController {
    @Autowired
    private RelationshipService relationshipService;

    @GetMapping("/relationship")
    public List<Relationship> getAllRelationships() {
        return relationshipService.getAllRelationships();
    }

    @PostMapping("/relationship/getRelationshipbyId")
    public Relationship getRelationshipById(@Valid @RequestBody RelationshipPK relationshipPK) {
        return relationshipService.getRelationshipById(relationshipPK).get();
    }

    //Cau1
    //{"friends":["len6","len2"]} with RequestFriends
    //{"userEmail": "len3","friendEmail": "len1" } with RelationshipPK
    @PostMapping("/relationship")
    public ResponseEntity<Map<String, Object>> beFriends(@Valid @RequestBody RequestFriends requestFriends) throws RelationshipException {
        boolean success = relationshipService.beFriends(requestFriends.getEmails().get(0), requestFriends.getEmails().get(1));
//        ResponseFriends responseFriends = new ResponseFriends();
//        responseFriends.setSuccess(success);
//        return responseFriends;
        Map<String, Object> body = new HashMap<>();
        body.put("Success", success);
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    //Cau2
    //len3 with String
    //{"email":"len3"} with RequestFriendsList
    @PostMapping("relationship/friendsList")
    public ResponseEntity<Map<String, Object>> getFriendList(@Valid @RequestBody RequestFriendsList requestFriendsList) {
        List<String> lst = relationshipService.getFriendsList(requestFriendsList.getEmail());
//        ResponseFriends responseFriends = new ResponseFriends();
//        responseFriends.setSuccess(true);
//        responseFriends.setCount(lst.size());
//        responseFriends.setFriends(lst);
//        return responseFriends;
        Map<String, Object> body = new HashMap<>();
        body.put("Success", true);
        body.put("Count", lst.size());
        body.put("friends", lst);
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @GetMapping("relationship/friendsList/{id}")
    public List<String> getFriendsList(@PathVariable(value = "id") String email) {
        return relationshipService.getFriendsList(email);
    }

    //Cau3
    //["len3","len5"] with List<String>
    @PostMapping("relationship/commonFriendsList")
    public ResponseEntity<Map<String, Object>> getCommonFriendsList(@Valid @RequestBody RequestFriends requestFriends) throws RelationshipException {
        List<String> lst = relationshipService.getCommonFriendsList(requestFriends.getEmails());
        Map<String, Object> body = new HashMap<>();
        body.put("Success", true);
        body.put("Count", lst.size());
        body.put("friends", lst);
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    //Cau4
    //{"requestor":"len3", "target":"len1"} with request
    @PostMapping("relationship/beSubcriber")
    public ResponseEntity<Map<String, Object>> beSubcriber(@Valid @RequestBody RequestSubcriber requestSubcriber) throws RelationshipException {
        boolean success = relationshipService.beSubciber(requestSubcriber.getRequestor(), requestSubcriber.getTarget());
        Map<String, Object> body = new HashMap<>();
        body.put("Success", success);
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    //Cau5
    @PostMapping("relationship/toBlock")
    public ResponseEntity<Map<String, Object>> toBlock(@Valid @RequestBody RequestSubcriber requestSubcriber) throws RelationshipException {
        boolean success = relationshipService.toBlock(requestSubcriber.getRequestor(), requestSubcriber.getTarget());
        Map<String, Object> body = new HashMap<>();
        body.put("Success", success);
        return new ResponseEntity<>(body, HttpStatus.OK);    }

    //Cau6
    //{"sender":"len3"}
    @PostMapping("relationship/receiveUpdate")
    public ResponseEntity<Map<String, Object>> getReceiveUpdateList(@Valid @RequestBody RequestReciveUpdate requestReciveUpdate) {
        List<String> lst = relationshipService.getReceiveUpdateList(requestReciveUpdate.getSender());
        Map<String, Object> body = new HashMap<>();
        body.put("Success", true);
        body.put("recipients", lst);
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @GetMapping("relationship/receiveUpdate/{id}")
    public List<String> getReceiveUpdateList(@PathVariable(value = "id") String email) {
        return relationshipService.getReceiveUpdateList(email);
    }
}
