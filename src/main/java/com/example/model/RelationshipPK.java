package com.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Email;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class RelationshipPK implements Serializable {
//    @Email
    @Column(name = "user_email")
    private String userEmail;
//    @Email
    @Column(name = "friend_email")
    private String friendEmail;
}
