package com.example.model.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "sender", "text" })
public class RequestReciveUpdate {
    @JsonProperty("sender")
    @NotEmpty
    @Email(message = "email error")
    @NotNull(message = "not null for email")
    private String sender;

    @JsonProperty("text")
    private String text;
}
