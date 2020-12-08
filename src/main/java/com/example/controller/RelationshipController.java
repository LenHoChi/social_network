package com.example.controller;

import com.example.dto.RelationshipDTO;
import com.example.exception.RelationshipException;
import com.example.model.Relationship;
import com.example.model.RelationshipPK;
import com.example.service.RelationshipService;
import com.example.model.request.RequestFriends;
import com.example.model.request.RequestFriendsList;
import com.example.model.request.RequestReciveUpdate;
import com.example.model.request.RequestSubcriber;
import com.example.model.response.ResponseFriends;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/relationship")
public class RelationshipController {
    @Autowired
    private RelationshipService relationshipService;

    @GetMapping("")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<?> findAllRelationships() {
        List<RelationshipDTO> listRelationship = relationshipService.findAllRelationships();
        if(listRelationship.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(relationshipService.findAllRelationships());
    }

    //{"userEmail": "newmooncsu1@gmail.com","friendEmail": "newmooncsu2@gmail.com"}
    @PostMapping("/find-relationship-by-id")
    public ResponseEntity<?> findRelationshipById(@Valid @RequestBody RelationshipPK relationshipPK) throws Exception {
        return ResponseEntity.ok(relationshipService.findRelationshipById(relationshipPK));
    }

    //Cau1
    //{"friends":["newmooncsu1@gmail.com","newmooncsu2@gmail.com"]} with RequestFriends
    //{"userEmail": "newmooncsu1@gmail.com","friendEmail": "newmooncsu2@gmail.com" } with RelationshipPK
    @PostMapping("")
    public ResponseEntity<?> beFriends(@RequestBody @Valid RequestFriends requestFriends) throws Exception {
        boolean success = relationshipService.beFriends(requestFriends.getEmails().get(0), requestFriends.getEmails().get(1));
        ResponseFriends responseFriends = ResponseFriends.builder().success(success).build();
        return ResponseEntity.ok(responseFriends);
//        Map<String, Object> body = new HashMap<>();
//        body.put("Success", success);
//        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    //Cau2
    //len3 with String
    //{"email":"newmooncsu1@gmail.com"} with RequestFriendsList
    @PostMapping("/friends-list")
    public ResponseEntity<?> findFriendList(@Valid @RequestBody RequestFriendsList requestFriendsList) {
        List<String> lstFriend = relationshipService.findFriendsList(requestFriendsList.getEmail());
        ResponseFriends responseFriends = new ResponseFriends();
        responseFriends.setSuccess(true);
        responseFriends.setCount(lstFriend.size());
        responseFriends.setFriends(lstFriend);
        return ResponseEntity.ok(responseFriends);
    }

    //Cau3
    //["newmooncsu1@gmail.com","newmooncsu3@gmail.com"] with List<String>
    //{"friends":["newmooncsu1@gmail.com","newmooncsu3@gmail.com"]}
    @PostMapping("/common-friends-list")
    public ResponseEntity<?> findCommonFriendsList(@Valid @RequestBody RequestFriends requestFriends) throws Exception {
        List<String> lstFriendCommon = relationshipService.findCommonFriendsList(requestFriends.getEmails());
        ResponseFriends responseFriends = new ResponseFriends();
        responseFriends.setSuccess(true);
        responseFriends.setCount(lstFriendCommon.size());
        responseFriends.setFriends(lstFriendCommon);
        return ResponseEntity.ok(responseFriends);
    }

    //Cau4
    //{"requestor":"newmooncsu1@gmail.com", "target":"newmooncsu2@gmail.com"} with request
    @PostMapping("/be-subscriber")
    public ResponseEntity<?> beSubscriber(@Valid @RequestBody RequestSubcriber requestSubcriber) throws RelationshipException {
        Relationship success = relationshipService.beSubscriber(requestSubcriber.getRequestor(), requestSubcriber.getTarget());
        ResponseFriends responseFriends = new ResponseFriends();
        responseFriends.setSuccess(true);
        return ResponseEntity.ok(responseFriends);
    }

    //Cau5
    @PostMapping("/to-block")
    public ResponseEntity<?> toBlock(@Valid @RequestBody RequestSubcriber requestSubcriber) throws RelationshipException {
        Relationship success = relationshipService.toBlock(requestSubcriber.getRequestor(), requestSubcriber.getTarget());
        ResponseFriends responseFriends = new ResponseFriends();
        responseFriends.setSuccess(true);
        return ResponseEntity.ok(responseFriends);
    }

    //Cau6
    //{"sender":"newmooncsu1@gmail.com", "text":"ho fgf dfd@gmail.com"}
    @PostMapping("/receive-update")
    public ResponseEntity<?> findReceiveUpdateList(@Valid @RequestBody RequestReciveUpdate requestReciveUpdate) {
        List<String> lstRecipient = relationshipService.findReceiveUpdateList(requestReciveUpdate.getSender(),requestReciveUpdate.getText());
        ResponseFriends responseFriends = new ResponseFriends();
        responseFriends.setSuccess(true);
        responseFriends.setRecipients(lstRecipient);
        return ResponseEntity.ok(responseFriends);
    }
}
