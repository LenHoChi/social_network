package com.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class RelationshipPK implements Serializable {
    @Column(name = "user_email")
    private String userEmail;
    @Column(name = "friend_email")
    private String friendEmail;
}
