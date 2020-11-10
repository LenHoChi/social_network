package com.example.utils.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "sender", "text" })
public class RequestReciveUpdate {
    @JsonProperty("sender")
//    @Email
    @NotBlank
    private String sender;

    @JsonProperty("text")
    private String text;
}
