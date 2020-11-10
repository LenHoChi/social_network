package com.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "relationship", schema = "public")
@Data
//@IdClass(Friends.class)
@NoArgsConstructor
@AllArgsConstructor
public class Relationship {
    @EmbeddedId
    private RelationshipPK relationshipPK;
    @Column(name = "arefriends")
    private Boolean areFriends;
    @Column(name = "issubcriber")
    private Boolean isSubcriber;
    @Column(name = "isblock")
    private Boolean isBlock;
}
