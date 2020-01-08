package com.marvel.gps.web.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
public class LoginDTO {

    @NotBlank
    @Size(min = 3, max = 60)
    private String userName;

    @NotBlank
    @Size(min = 3, max = 60)
    private String password;
}
