package com.marvel.gps.web.dto.request;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private String userName;

    private String password;

    private String email;

    private String mobileNo;

    private String role;

}
