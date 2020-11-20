package com.example.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.IndexColumn;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "users", schema = "public")
@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class User implements Serializable {
    @Id
    @Column(name = "email")
    @Email(message = "error email model")
    @NotBlank(message = "not empty")
    @NonNull
    private String email;

    @OneToMany(fetch = FetchType.EAGER,mappedBy = "user", cascade = CascadeType.ALL)
    //@OneToMany(mappedBy = "user")
    @JsonIgnoreProperties("user")
    private Set<Relationship> relationships;
}
