package com.marvel.gps.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class GPSPasswordEncoder {

    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
