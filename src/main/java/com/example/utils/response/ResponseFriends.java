package com.example.utils.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"success", "friends", "count", "recipients", "errorMessage"})
@Data
public class ResponseFriends {
    @JsonProperty("success")
    private boolean success;
    @JsonProperty("friends")
    private List<String> friends;
    @JsonProperty("count")
    private Integer count;
    @JsonProperty("recipients")
    private List<String> recipients;
    @JsonProperty("errorMessage")
    private List<String> errorMessage;
}
