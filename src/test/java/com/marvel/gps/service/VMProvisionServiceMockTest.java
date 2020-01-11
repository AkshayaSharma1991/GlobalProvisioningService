package com.marvel.gps.service;

import com.marvel.gps.exception.UserException;
import com.marvel.gps.exception.VMProvisionException;
import com.marvel.gps.model.GPSUser;
import com.marvel.gps.model.Role;
import com.marvel.gps.model.RoleName;
import com.marvel.gps.model.UserVMProvison;
import com.marvel.gps.repository.GPSUserRepository;
import com.marvel.gps.repository.UserVMProvisionRepository;
import com.marvel.gps.security.GPSPasswordEncoder;
import static org.mockito.ArgumentMatchers.any;

import com.marvel.gps.serviceimpl.UserServiceImpl;
import com.marvel.gps.serviceimpl.VMProvisionServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;
import static org.junit.Assert.assertEquals;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class VMProvisionServiceMockTest {

    @Mock
    UserVMProvisionRepository provisionRepository;

    @Mock
    UserServiceImpl userService;

    @InjectMocks
    VMProvisionServiceImpl provisionService;

    private static final GPSPasswordEncoder encoder = new GPSPasswordEncoder();

    @Before
    public void setUp(){
        provisionService.setUserService(userService);
    }

    @Test
    public void TestCreateVMForUser() throws UserException, VMProvisionException {
        String userName = "TestUser";
        String os = "Mac OS";
        int ram = 256;
        int hardDisk = 1028;
        int cpuCore = 32;
        GPSUser user = new GPSUser(userName, encoder.passwordEncoder().encode("testUser"),"testUser@gmail.com","1234567890", new Role(RoleName.ROLE_USER));
        when(userService.loadUserByUserName(anyString())).thenReturn(user);
        when(provisionRepository.save(any(UserVMProvison.class))).thenReturn(new UserVMProvison(os, ram, hardDisk, cpuCore, user));
        assertEquals(true, provisionService.createVMForUser(os,ram, hardDisk, cpuCore, userName, ""));
    }

    @Test
    public void TestGetVMListForUser() throws VMProvisionException, UserException {
        String userName = "TestUser";
        String os = "Mac OS";
        int ram = 256;
        int hardDisk = 1028;
        int cpuCore = 32;
        GPSUser user = new GPSUser(userName, encoder.passwordEncoder().encode("testUser"),"testUser@gmail.com","1234567890", new Role(RoleName.ROLE_USER));
        when(userService.loadUserByUserName(anyString())).thenReturn(user);
        when(provisionRepository.findByGPSUser_userId(anyLong())).thenReturn(Stream.of(new UserVMProvison(os, ram, hardDisk, cpuCore, user), new UserVMProvison(os, ram, hardDisk, cpuCore, user), new UserVMProvison(os, ram, hardDisk, cpuCore, user)).collect(Collectors.toList()));
        assertEquals(3, provisionService.getVMListForUser(userName, "").size());
    }


    @Test
    public void TestGetVMListForUser_RAMDESC() throws VMProvisionException, UserException {
        String userName = "TestUser";
        String os = "Mac OS";
        int ram = 256;
        int hardDisk = 1028;
        int cpuCore = 32;
        GPSUser user = new GPSUser(userName, encoder.passwordEncoder().encode("testUser"),"testUser@gmail.com","1234567890", new Role(RoleName.ROLE_USER));
        when(userService.loadUserByUserName(anyString())).thenReturn(user);
        when(provisionRepository.getVMListForUser_RAMDESC(anyLong(), anyInt())).thenReturn(Stream.of(new UserVMProvison(os, ram, hardDisk, cpuCore, user), new UserVMProvison(os, ram, hardDisk, cpuCore, user), new UserVMProvison(os, ram, hardDisk, cpuCore, user)).collect(Collectors.toList()));
        assertEquals(3, provisionService.getVMListForUser_RAMDESC(userName, 4,"").size());
    }

    @Test
    public void TestGetVMList_RAMDESC() throws VMProvisionException, UserException {
        String userName = "TestUser";
        String os = "Mac OS";
        int ram = 256;
        int hardDisk = 1028;
        int cpuCore = 32;
        GPSUser user = new GPSUser(userName, encoder.passwordEncoder().encode("testUser"),"testUser@gmail.com","1234567890", new Role(RoleName.ROLE_USER));
        when(provisionRepository.getVMList_RAMDESC(anyInt())).thenReturn(Stream.of(new UserVMProvison(os, ram, hardDisk, cpuCore, user), new UserVMProvison(os, ram, hardDisk, cpuCore, user), new UserVMProvison(os, ram, hardDisk, cpuCore, user)).collect(Collectors.toList()));
        assertEquals(3, provisionService.getVMList_RAMDESC(4,"").size());
    }
}
