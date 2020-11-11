package com.example.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "relationship", schema = "public")
@Data
//@IdClass(Friends.class)
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class Relationship {
    @EmbeddedId
    @NonNull
    private RelationshipPK relationshipPK;
    @Column(name = "arefriends")
    @NonNull
    private Boolean areFriends;
    @Column(name = "issubcriber")
    @NonNull
    private Boolean isSubcriber;
    @Column(name = "isblock")
    @NonNull
    private Boolean isBlock;

    @ManyToOne
    @JoinColumn(name = "user_email", insertable = false, updatable = false)
    //@MapsId("userEmail")
    @JsonIgnoreProperties("relationships")
    @EqualsAndHashCode.Exclude private User user;
}
