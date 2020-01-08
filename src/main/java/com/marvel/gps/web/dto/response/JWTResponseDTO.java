package com.marvel.gps.web.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class JWTResponseDTO {

    private String token;
    private String type = "Bearer";

    public JWTResponseDTO(String accessToken) {
        this.token = accessToken;
    }
}
