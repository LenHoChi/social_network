package com.example.service.impl;

import com.example.model.Friends;
import com.example.repository.FriendsRepository;
import com.example.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class FriendServiceImpl implements FriendService {
    @Autowired
    FriendsRepository friendsRepository;
    @Override
    public List<Friends> getAllFriends() {
        return friendsRepository.findAll();
    }

    @Override
    public Friends saveFriend(Friends friends) {
        return friendsRepository.save(friends);
    }

    @Override
    public Friends updateFriend(Friends friends, Integer userId, Integer friendId) {
        return null;
    }

    @Override
    public Map<String, Boolean> deleteFriend(Integer userId, Integer friendId) {
        return null;
    }
}
