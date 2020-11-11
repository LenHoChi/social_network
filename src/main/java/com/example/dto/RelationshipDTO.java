package com.example.dto;

import com.example.model.RelationshipPK;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RelationshipDTO {
    private RelationshipPK relationshipPK;
    private Boolean areFriends;
    private Boolean isSubcriber;
    private Boolean isBlock;
}
