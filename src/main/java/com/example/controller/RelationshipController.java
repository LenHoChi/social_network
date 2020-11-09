package com.example.controller;

import com.example.model.Relationship;
import com.example.model.RelationshipPK;
import com.example.service.RelationshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class RelationshipController {
    @Autowired
    private RelationshipService relationshipService;
    @PostMapping("/relationship/friend")
    public Relationship getFriendById(@Valid @RequestBody RelationshipPK relationshipPK){
        return relationshipService.getRelationshipById(relationshipPK).get();
    }
    @GetMapping("/relationship")
    public List<Relationship> getAllRelationships(){
        return relationshipService.getAllRelationships();
    }
    @PostMapping("/relationship")
    public Boolean beFriends(@Valid @RequestBody RelationshipPK relationshipPK){
        return relationshipService.beFriends(relationshipPK.getUserEmail(),relationshipPK.getFriendEmail());
    }
//    @PostMapping("relationship/listFriend")
//    public List<String> getFriendList(@Valid @RequestBody String email){
//        return relationshipService.getFriendList(email);
//    }
    @GetMapping("relationship/listFriend/{id}")
    public List<String> getFriendList(@PathVariable(value = "id") String email){
        return relationshipService.getFriendList(email);
    }
    @PostMapping("relationship/commonListFriend")
    public List<String> getCommonFriendList(@Valid @RequestBody List<String> emails){
        return relationshipService.getCommonFriendList(emails);
    }
}
