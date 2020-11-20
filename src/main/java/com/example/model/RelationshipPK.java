package com.example.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
//@RequiredArgsConstructor
public class RelationshipPK implements Serializable {
    //    @NonNull
    @Column(name = "user_email")
    @Email(message = "error email")
    @NotBlank(message = "not empty")
    @NotNull(message = "not null for this data")
    private String userEmail;
    //    @NonNull
    @Column(name = "friend_email")
    @Email(message = "error email")
    @NotBlank(message = "not empty")
    @NotNull(message = "not null for this data")
    private String friendEmail;


}
