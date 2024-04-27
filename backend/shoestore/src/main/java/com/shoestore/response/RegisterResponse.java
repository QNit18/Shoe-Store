package com.shoestore.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shoestore.models.User;
import jakarta.persistence.MappedSuperclass;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
@Builder
public class RegisterResponse {
    @JsonProperty("messages")
    private String message;

    @JsonProperty("user")
    private User user;
}
