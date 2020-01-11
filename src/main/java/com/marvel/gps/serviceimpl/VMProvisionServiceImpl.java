package com.marvel.gps.serviceimpl;

import com.marvel.gps.exception.UserException;
import com.marvel.gps.exception.VMProvisionException;
import com.marvel.gps.model.GPSUser;
import com.marvel.gps.model.UserVMProvison;
import com.marvel.gps.repository.UserVMProvisionRepository;
import com.marvel.gps.service.UserService;
import com.marvel.gps.service.VMProvisionService;
import com.marvel.gps.util.LoggingUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VMProvisionServiceImpl implements VMProvisionService {

    @Autowired
    UserVMProvisionRepository provisionRepository;

    @Autowired
    UserService userService;

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    private static final LoggingUtil LOG = new LoggingUtil(VMProvisionServiceImpl.class);

    public boolean createVMForUser(String os, int ram, int hardDisk, int cpuCore, String userName, String correlationId) throws VMProvisionException, UserException {
            LOG.info("Retrieving user based on userName: " + userName, correlationId);
            GPSUser user = userService.loadUserByUserName(userName);
            LOG.info("Retrieving user based on userName: " + userName
                    + " successful", correlationId);
            UserVMProvison provisionForUser = new UserVMProvison(os, ram, hardDisk, cpuCore, user);
            provisionRepository.save(provisionForUser);
            LOG.info(String.format("Saved VM successfully for user: %s, with following specifications.  OS: %s, RAM: %s, Hard Disk: %s, cpuCore: %s", userName, os, ram, hardDisk, cpuCore), correlationId);

        return true;
    }

    @Override
    public List<UserVMProvison> getVMListForUser(String userName, String correlationId) throws UserException, VMProvisionException{
        return provisionRepository.findByGPSUser_userId(userService.loadUserByUserName(userName).getUserId());
    }

    @Override
    public List<UserVMProvison> getVMListForUser_RAMDESC(String userName, int count, String correlationId) throws UserException, VMProvisionException {
        return provisionRepository.getVMListForUser_RAMDESC(userService.loadUserByUserName(userName).getUserId(), count);
    }

    @Override
    public List<UserVMProvison> getVMList_RAMDESC(int count, String correlationId) throws VMProvisionException{
        return provisionRepository.getVMList_RAMDESC(count);
    }
}
