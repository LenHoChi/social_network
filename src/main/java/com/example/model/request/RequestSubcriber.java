package com.example.model.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "requestor", "target" })
public class RequestSubcriber {
    @JsonProperty("requestor")
    @NotEmpty
    @Email(message = "email error")
    @NotNull(message = "not null for email")
    private String requestor;

    @JsonProperty("target")
    @NotEmpty
    @Email(message = "email error")
    @NotNull(message = "not null for email")
    private String target;
}
