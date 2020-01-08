package com.marvel.gps.web.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserDTO {

    private String userName;

    private String password;

    private String email;

    private String mobileNo;

    private String role;

}
