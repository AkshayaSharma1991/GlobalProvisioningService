package com.marvel.gps.service;

import com.marvel.gps.exception.UserException;
import com.marvel.gps.exception.VMProvisionException;
import com.marvel.gps.model.UserVMProvison;

import java.util.List;

public interface VMProvisionService {

    public boolean createVMForUser(String os, int ram, int hardDisk, int cpuCore, String userName, String correlationId) throws UserException, VMProvisionException;

    public List<UserVMProvison> getVMListForUser(String userName, String correlationId) throws UserException, VMProvisionException;

    public List<UserVMProvison> getVMListForUser_RAMDESC(String userName, int count, String correlationId) throws UserException, VMProvisionException;

    public List<UserVMProvison> getVMList_RAMDESC(int count, String correlationId) throws VMProvisionException;

}
