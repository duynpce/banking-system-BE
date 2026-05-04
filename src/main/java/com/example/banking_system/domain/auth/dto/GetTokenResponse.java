package com.example.banking_system.domain.auth.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetTokenResponse {
    @JsonAlias(value = "access_token")
    private String accessToken;

    @JsonAlias(value = "refresh_token")
    private String refreshToken;

    @JsonAlias(value = "id_token")
    private String idToken;

}
