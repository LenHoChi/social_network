package com.example.model.request;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.bytebuddy.implementation.bind.annotation.Empty;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "friends"
})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestFriends {

    @JsonProperty("friends")
    @Size(min = 2, max= 2, message = "size must be 2")

    private List<@NotNull(message = "not null for email") @Email(message = "email fail") @NotEmpty(message = "not empty") String> emails;
}