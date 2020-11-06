package com.example.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "friends",schema = "public")
@Data
public class Friends {
    @Column(name = "user_id")
    private String userId;
    @Column(name = "friend_id")
    private String friendId;
}
