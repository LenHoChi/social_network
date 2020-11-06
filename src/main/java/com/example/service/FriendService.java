package com.example.service;

import com.example.model.Friends;
import org.hibernate.sql.Update;

import java.util.List;
import java.util.Map;

public interface FriendService {
    List<Friends> getAllFriends();
    Friends saveFriend(Friends friends);
    Friends updateFriend(Friends friends, Integer userId, Integer friendId);
    Map<String, Boolean> deleteFriend(Integer userId, Integer friendId);
}
