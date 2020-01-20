package com.marvel.gps.web.controller;

import com.marvel.gps.constants.GPSConstants;
import com.marvel.gps.model.UserVMProvison;
import com.marvel.gps.service.VMProvisionService;
import com.marvel.gps.util.LoggingUtil;
import com.marvel.gps.web.dto.request.VMProvisionRequest;
import com.marvel.gps.web.dto.response.ServiceResponseDTO;
import com.marvel.gps.web.dto.response.VMDetailsResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@Validated
@Api(value = "VM Provision", description = "VM Provisioning for GPS", tags = { "VM Provisioning and Management" })
@RequestMapping(value = "/gps/provisionService")
public class ProvisioningController {

    private static final LoggingUtil LOG = new LoggingUtil(ProvisioningController.class);
    @Autowired
    VMProvisionService provisionService;

    @PostMapping(value = "/requestVM")
    @ApiOperation(value="Request VM For User", tags = { "VM Provisioning and Management" })
    public ResponseEntity<?> requestVM(@Valid @RequestBody VMProvisionRequest request) {

        String correlationId = generateUniqueCorrelationId();
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        String username = userDetails.getUsername();
        LOG.info("VM Provision request received for user: " + username, correlationId);
        try {
            provisionService.createVMForUser(request.getOS(), request.getRam(), request.getHardDisk(), request.getCpuCore(), username, correlationId);
            ServiceResponseDTO responseDTO = new ServiceResponseDTO(GPSConstants.SERVICE_RESPONSE_SUCCESS, "VM Created successfully for user: " + username);
            LOG.info("VM Provision successfully completed for user: " + username, correlationId);
            return new ResponseEntity<ServiceResponseDTO>(responseDTO, HttpStatus.OK);
        } catch (Exception e){
            ServiceResponseDTO responseDTO = new ServiceResponseDTO(GPSConstants.SERVICE_RESPONSE_FAILURE, "VM Creation failed for user: " + username + " .  Reason: " + e.getMessage());
            LOG.info("VM Provision failed for user: " + username + " .  Reason: " + e.getMessage(), correlationId);
            return new ResponseEntity<ServiceResponseDTO>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping(value = "/listVMsForSelf")
    @ApiOperation(value="Get all VMs For Self", tags = { "VM Provisioning and Management" })
    public ResponseEntity<?> listVMForSelf(){
        String correlationId = generateUniqueCorrelationId();
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        String userName = userDetails.getUsername();
        LOG.info("Request received to list VM for user: " + userName, correlationId);
        try{
            List<UserVMProvison> vmList = provisionService.getVMListForUser(userName, correlationId);
            LOG.info("VM List received successfully for user: " + userName, correlationId);
            return new ResponseEntity<List<VMDetailsResponse>>(buildResponseList(vmList), HttpStatus.OK);
        } catch (Exception e){
            LOG.error("VM List fetch failed for user: " + userName, correlationId);
            return new ResponseEntity<ServiceResponseDTO>(new ServiceResponseDTO(GPSConstants.SERVICE_RESPONSE_FAILURE, "VM List fetch failed for user: " + userName + " .  Reason: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping(value = "/listVMsForUser")
    @ApiOperation(value="Get all VMs For User (Role Required: MASTER)", tags = { "VM Provisioning and Management" })
    @PreAuthorize("hasAuthority('ROLE_MASTER')")
    public ResponseEntity<?> listVMForUser(@RequestParam String userName){
        String correlationId = generateUniqueCorrelationId();
        LOG.info("Request received to list VM for user: " + userName, correlationId);
        try{
            List<UserVMProvison> vmList = provisionService.getVMListForUser(userName, correlationId);
            LOG.info("VM List received successfully for user: " + userName, correlationId);
            return new ResponseEntity<List<VMDetailsResponse>>(buildResponseList(vmList), HttpStatus.OK);
        } catch (Exception e){
            LOG.error("VM List fetch failed for user: " + userName, correlationId);
            return new ResponseEntity<ServiceResponseDTO>(new ServiceResponseDTO(GPSConstants.SERVICE_RESPONSE_FAILURE, "VM List fetch failed for user: " + userName + " .  Reason: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping(value = "/listTopN_VMsForUserBasedOnRAM")
    @ApiOperation(value="Get top N VMs for user based on Memory", tags = { "VM Provisioning and Management" })
    public ResponseEntity<?> getVMBasedOnUserAndRAM(@RequestParam int count){
        String correlationId = generateUniqueCorrelationId();
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        String userName = userDetails.getUsername();
        LOG.info("Request received to list VM based on RAM for user: " + userName, correlationId);
        try{
            List<UserVMProvison> vmList = provisionService.getVMListForUser_RAMDESC(userName, count, correlationId);
            LOG.info("VM List based on RAM received successfully for user: " + userName, correlationId);
            return new ResponseEntity<List<VMDetailsResponse>>(buildResponseList(vmList), HttpStatus.OK);
        } catch (Exception e){
            LOG.error("VM List based on RAM fetch failed for user: " + userName, correlationId);
            return new ResponseEntity<ServiceResponseDTO>(new ServiceResponseDTO(GPSConstants.SERVICE_RESPONSE_FAILURE, "VM List fetch based on RAM failed for user: " + userName + " .  Reason: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/listTopN_VMsBasedOnRAM")
    @PreAuthorize("hasAuthority('ROLE_MASTER')")
    @ApiOperation(value="Get top N VMs based on Memory (Role Required: MASTER)", tags = { "VM Provisioning and Management" })
    public ResponseEntity<?> getVMBasedOnRAM(@RequestParam int count){
        String correlationId = generateUniqueCorrelationId();
        try{
            List<UserVMProvison> vmList = provisionService.getVMList_RAMDESC(count, correlationId);
            LOG.info("VM List based on RAM received successfully", correlationId);
            return new ResponseEntity<List<VMDetailsResponse>>(buildResponseList(vmList), HttpStatus.OK);
        } catch (Exception e){
            LOG.error("VM List based on RAM fetch failed", correlationId);
            return new ResponseEntity<ServiceResponseDTO>(new ServiceResponseDTO(GPSConstants.SERVICE_RESPONSE_FAILURE, "VM List fetch based on RAM failed.  Reason: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private String generateUniqueCorrelationId() {
        return UUID.randomUUID().toString();
    }

    private List<VMDetailsResponse> buildResponseList(List<UserVMProvison> vmList){
        List<VMDetailsResponse> responseList = new ArrayList<>();
        vmList.forEach(vm -> {
            VMDetailsResponse response = new VMDetailsResponse(vm.getProvisionId(), vm.getOs(), vm.getRam(), vm.getHardDisk(), vm.getCpuCores(), vm.getGPSUser().getUserId(), vm.getGPSUser().getUserName());
            responseList.add(response);
        });

        return responseList;
    }

    @ExceptionHandler(value= MethodArgumentTypeMismatchException.class)
    void handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e, HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value(), String.format("Invalid Parameter to %s, parameter: %s invalid.  Reason: Invalid data passed in parameter", e.getParameter(), e.getName()));
    }

}
