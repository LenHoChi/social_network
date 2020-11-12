package com.example.utils.request;

import java.util.List;

import javax.persistence.Column;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


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
    private List<String> emails;
}