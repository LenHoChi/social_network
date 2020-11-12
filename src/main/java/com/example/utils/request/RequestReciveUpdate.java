package com.example.utils.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "sender", "text" })
@RequiredArgsConstructor
public class RequestReciveUpdate {
    @JsonProperty("sender")
//    @Email
    @NotBlank
    @NonNull
    private String sender;

    @JsonProperty("text")
    private String text;
}
