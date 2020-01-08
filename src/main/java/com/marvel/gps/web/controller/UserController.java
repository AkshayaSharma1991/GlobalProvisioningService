package com.marvel.gps.web.controller;

import com.marvel.gps.constants.GPSConstants;
import com.marvel.gps.exception.UserException;
import com.marvel.gps.model.GPSUser;
import com.marvel.gps.model.Role;
import com.marvel.gps.model.RoleName;
import com.marvel.gps.repository.GPSUserRepository;
import com.marvel.gps.repository.RoleRepository;
import com.marvel.gps.security.GPSPasswordEncoder;
import com.marvel.gps.security.jwt.JWTProvider;
import com.marvel.gps.service.UserService;
import com.marvel.gps.util.LoggingUtil;
import com.marvel.gps.web.dto.request.LoginDTO;
import com.marvel.gps.web.dto.request.UserDTO;
import com.marvel.gps.web.dto.response.JWTResponseDTO;
import com.marvel.gps.web.dto.response.ServiceResponseDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Api(value = "User", description = "User management for GPS", tags = { "User Management" })
public class UserController {

    private static final LoggingUtil LOG = new LoggingUtil(UserController.class);

    @Autowired
    GPSPasswordEncoder encoder;

    @Autowired
    UserService userService;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    GPSUserRepository userRepository;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JWTProvider jwtProvider;

    @PostMapping(value = "/user/signup")
    @ApiOperation(value="Create new user", tags = { "User Management" })
    public ResponseEntity<?> createNewUser(@RequestBody UserDTO user) {
        ServiceResponseDTO responseDTO = null;
        LOG.info("Checking if username is already registered.  User Name " + user.getUserName(), "");
        if (userRepository.findByUserName(user.getUserName()) != null) {
            LOG.error("User already exists.  User name available error. ", "");
            return new ResponseEntity<String>("User name already registered.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        LOG.info("Checking if email is already registered.  Email Id: " + user.getEmail(), "");
        if (userRepository.findByEmail(user.getEmail()) != null) {
            LOG.error("User already exists.  Email available error. ", "");
            return new ResponseEntity<String>("User Email already registered.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        LOG.info("Checking if mobile number is already registered.  Mobile No: " + user.getMobileNo(), "");
        if (userRepository.findByMobileNo(user.getMobileNo()) != null) {
            LOG.error("User already exists.  Mobile number available error. ", "");
            return new ResponseEntity<String>("User Mobile already registered.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        LOG.info("User does not exist in the system.  Creating user: " + user.getUserName(), "");
        GPSUser gpsUser = new GPSUser();
        gpsUser.setEmail(user.getEmail());
        Role role;
        String strRole = user.getRole();
        {
            switch (strRole) {
                case "MASTER":
                    Role masterRole = roleRepository.findByName(RoleName.ROLE_MASTER)
                            .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not found."));
                    role = masterRole;

                    break;
                case "USER":
                    Role nonMasterRole = roleRepository.findByName(RoleName.ROLE_USER)
                            .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not found."));
                    role = nonMasterRole;

                    break;
                default:
                    Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                            .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not found."));
                    role = userRole;
            }
        }
        gpsUser.setRole(role);
        gpsUser.setUserName(user.getUserName());
        gpsUser.setPassword(encoder.passwordEncoder().encode(user.getPassword()));
        gpsUser.setMobileNo(user.getMobileNo());

        try {
            gpsUser = userService.createUser(gpsUser);
            LOG.info("User creation successful.  Id: " + gpsUser.getUserId(), "");
            responseDTO = new ServiceResponseDTO(GPSConstants.SERVICE_RESPONSE_SUCCESS, "User creation successful.  Id: " + gpsUser.getUserId());
            return new ResponseEntity<ServiceResponseDTO>(responseDTO, HttpStatus.OK);
        } catch (UserException e){
            LOG.error("User creation resulted in an error.  Reason: " + e.getMessage(), "");
            responseDTO = new ServiceResponseDTO(GPSConstants.SERVICE_RESPONSE_FAILURE, "User creation failed." + gpsUser.getUserId() + " Reason: " + e.getMessage());
            return new ResponseEntity<ServiceResponseDTO>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


    @PostMapping("/user/signin")
    @ApiOperation(value="User sign-in (or) JWT Token generation for user", tags = { "User Management" })
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginDTO loginRequest) {
        LOG.info("Generating JWT token for user: " + loginRequest.getUserName(), "");
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUserName(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateJwtToken(authentication);
        return ResponseEntity.ok(new JWTResponseDTO(jwt));
    }

    @DeleteMapping("/gps/delete/self")
    @ApiOperation(value="Delete logged-in user", tags = { "User Management" })
    public ResponseEntity<?> deleteSelf() {
        ServiceResponseDTO responseDTO = null;
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        String userName = userDetails.getUsername();

        return deleteUser(userName);
    }

    @DeleteMapping("/gps/delete/user")
    @ApiOperation(value="Delete any user (Role Required: MASTER)", tags = { "User Management" })
    @PreAuthorize("hasAuthority('ROLE_MASTER')")
    public ResponseEntity<?> deleteUserWithName(String userName) {
        ServiceResponseDTO responseDTO = null;
        return deleteUser(userName);
    }

    private ResponseEntity deleteUser(String userName){
        LOG.info("Request received for deleting user: " + userName, "");
        ServiceResponseDTO responseDTO = null;
        try {
            userService.deleteUser(userName);
            responseDTO = new ServiceResponseDTO(GPSConstants.SERVICE_RESPONSE_SUCCESS, "User deletion successful.  Name: " + userName);
            LOG.info("User deletion successful.  Deleted user: " + userName + " from GPS.", "");
            return new ResponseEntity<ServiceResponseDTO>(responseDTO, HttpStatus.OK);
        } catch (UserException e){
            responseDTO = new ServiceResponseDTO(GPSConstants.SERVICE_RESPONSE_FAILURE, "User deletion failed.  Reason: " + e.getMessage());
            LOG.error("User deletion resulted in an error.  Reason: " + e.getMessage(), "");
            return new ResponseEntity<ServiceResponseDTO>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
