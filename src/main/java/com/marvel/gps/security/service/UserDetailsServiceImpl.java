package com.marvel.gps.security.service;

import com.marvel.gps.exception.UserException;
import com.marvel.gps.model.GPSUser;
import com.marvel.gps.repository.GPSUserRepository;
import com.marvel.gps.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserService userService;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        try {
            GPSUser user = userService.loadUserByUserName(s);
            return UserPrinciple.build(user);
        } catch (UserException e) {
            throw new UsernameNotFoundException("User Not Found with -> username or email : " + s);
        }
    }
}
