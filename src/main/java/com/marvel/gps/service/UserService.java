package com.marvel.gps.service;

import com.marvel.gps.exception.UserException;
import com.marvel.gps.model.GPSUser;

public interface UserService {

    public GPSUser createUser(GPSUser user) throws UserException;

    public GPSUser loadUserByUserName(String username) throws UserException;

    public void deleteUser(String userName) throws UserException;
}
