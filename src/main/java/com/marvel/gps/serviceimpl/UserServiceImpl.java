package com.marvel.gps.serviceimpl;

import com.marvel.gps.exception.UserException;
import com.marvel.gps.model.GPSUser;
import com.marvel.gps.repository.GPSUserRepository;
import com.marvel.gps.repository.UserVMProvisionRepository;
import com.marvel.gps.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    GPSUserRepository userRepository;

    @Autowired
    UserVMProvisionRepository provisionRepository;

    @Override
    public GPSUser createUser(GPSUser user)throws UserException {
        return userRepository.save(user);
    }

    @Override
    public GPSUser loadUserByUserName(String username) throws UserException {
        GPSUser user = userRepository.findByUserName(username);
        if(user == null)
            throw new UserException(String.format("User name %s not found in GPS." , username));

        return user;
    }

    @Override
    public void deleteUser(String userName) throws UserException{
            GPSUser user = loadUserByUserName(userName);
            if(user == null)
                throw new UserException(String.format("User name %s not found in GPS." , userName));
            Long userId = user.getUserId();
            userRepository.deleteById(userId);
    }
}
