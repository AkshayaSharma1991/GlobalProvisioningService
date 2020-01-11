package com.marvel.gps.service;

import com.marvel.gps.exception.UserException;
import com.marvel.gps.model.GPSUser;
import com.marvel.gps.model.Role;
import com.marvel.gps.model.RoleName;
import com.marvel.gps.repository.GPSUserRepository;
import com.marvel.gps.security.GPSPasswordEncoder;
import com.marvel.gps.serviceimpl.UserServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceMockTest {

    @Mock
    GPSUserRepository userRepository;

    @InjectMocks
    UserServiceImpl userService;

    private static final GPSPasswordEncoder encoder = new GPSPasswordEncoder();

    @Test
    public void testCreateUser() throws UserException {
        GPSUser gpsUser = new GPSUser("TestUser", encoder.passwordEncoder().encode("testUser"),"testUser@gmail.com","1234567890", new Role(RoleName.ROLE_USER));
        when(userRepository.save(any(GPSUser.class))).thenReturn(gpsUser);
        GPSUser savedUser = userService.createUser(gpsUser);
        assertEquals(savedUser.getUserName(), gpsUser.getUserName());
    }

    @Test(expected = UserException.class)
    public void testCreateUser_ForNullUser() throws UserException{
        userService.createUser(null);
    }

    @Test
    public void testLoadByUserName() throws UserException{
        String userName = "TestUser";
        when(userRepository.findByUserName(userName)).thenReturn(new GPSUser("TestUser", encoder.passwordEncoder().encode("testUser"),"testUser@gmail.com","1234567890", new Role(RoleName.ROLE_USER)));
        GPSUser user = userService.loadUserByUserName(userName);
        assertEquals(user.getUserName(), userName);
        assertEquals(user.getEmail(), "testUser@gmail.com");
    }

    @Test(expected = UserException.class)
    public void testLoadByUserName_ForNullUserName() throws UserException{
        GPSUser user = userService.loadUserByUserName(null);
    }

    @Test
    public void testDeleteUser() throws UserException {
        String userName = "TestUser";
        GPSUser user = new GPSUser("TestUser", encoder.passwordEncoder().encode("testUser"),"testUser@gmail.com","1234567890", new Role(RoleName.ROLE_USER));
        when(userRepository.findByUserName(userName)).thenReturn(user);
        userService.deleteUser(userName);
        verify(userRepository,times(1)).findByUserName(userName);
        verify(userRepository,times(1)).deleteById(user.getUserId());
    }

    @Test(expected = UserException.class)
    public void testDeleteUser_ForNullUserName() throws UserException {
        userService.deleteUser(null);
    }

    @Test(expected = UserException.class)
    public void testDeleteUser_ForInvalidUserName() throws UserException {
        userService.deleteUser("DummyUser");
    }
}
