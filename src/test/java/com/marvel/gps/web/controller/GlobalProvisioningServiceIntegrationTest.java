package com.marvel.gps.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marvel.gps.web.dto.request.LoginDTO;
import com.marvel.gps.web.dto.request.UserDTO;
import com.marvel.gps.web.dto.request.VMProvisionRequest;
import com.marvel.gps.web.dto.response.JWTResponseDTO;
import com.marvel.gps.web.dto.response.ServiceResponseDTO;
import com.marvel.gps.web.dto.response.VMDetailsResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class GlobalProvisioningServiceIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    private static final ObjectMapper om = new ObjectMapper();

    private String jwtToken = "Bearer ";

    private static final String AUTH_HEADER = "Authorization";
    @Before
    public void setup(){
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    @Order(1)
    public void TestCreateUser() throws Exception {
        UserDTO userDTO = new UserDTO("TestUser", "testUser", "testUser@gmail.com", "23123123321", "MASTER");
        String jsonRequest = om.writeValueAsString(userDTO);
        MvcResult result = mockMvc.perform(post("/user/signup").content(jsonRequest).contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk()).andReturn();
        String resultContent = result.getResponse().getContentAsString();
        ServiceResponseDTO responseDTO = om.readValue(resultContent, ServiceResponseDTO.class);
        Assert.assertEquals("SUCCESS", responseDTO.getStatus());
    }

    @Test
    @Order(2)
    public void TestCreateUser_ARegisteredEmail() throws Exception {
        UserDTO userDTO = new UserDTO("TestUser1", "testUser", "testUser@gmail.com", "23123123321", "MASTER");
        String jsonRequest = om.writeValueAsString(userDTO);
        MvcResult result = mockMvc.perform(post("/user/signup").content(jsonRequest).contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().is5xxServerError()).andReturn();
        String resultContent = result.getResponse().getContentAsString();
        ServiceResponseDTO responseDTO = om.readValue(resultContent, ServiceResponseDTO.class);
        Assert.assertEquals("FAILURE", responseDTO.getStatus());
    }

    @Test
    @Order(3)
    public void TestCreateUser_BRegisteredUser() throws Exception {
        UserDTO userDTO = new UserDTO("TestUser", "testUser", "testUser@gmail.com", "23123123321", "MASTER");
        String jsonRequest = om.writeValueAsString(userDTO);
        MvcResult result = mockMvc.perform(post("/user/signup").content(jsonRequest).contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().is5xxServerError()).andReturn();
        String resultContent = result.getResponse().getContentAsString();
        ServiceResponseDTO responseDTO = om.readValue(resultContent, ServiceResponseDTO.class);
        Assert.assertEquals("FAILURE", responseDTO.getStatus());
    }

    @Test
    @Order(4)
    public void TestCreateUser_CRegisteredMobile() throws Exception {
        UserDTO userDTO = new UserDTO("TestUser1", "testUser", "testUser1@gmail.com", "23123123321", "MASTER");
        String jsonRequest = om.writeValueAsString(userDTO);
        MvcResult result = mockMvc.perform(post("/user/signup").content(jsonRequest).contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().is5xxServerError()).andReturn();
        String resultContent = result.getResponse().getContentAsString();
        ServiceResponseDTO responseDTO = om.readValue(resultContent, ServiceResponseDTO.class);
        Assert.assertEquals("FAILURE", responseDTO.getStatus());
    }

    @Test
    public void TestUserSignIn() throws Exception{
        LoginDTO login = new LoginDTO();
        login.setPassword("testUser");
        login.setUserName("TestUser");
        String jsonRequest = om.writeValueAsString(login);
        MvcResult result = mockMvc.perform(post("/user/signin").content(jsonRequest).contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk()).andReturn();
        String resultContent = result.getResponse().getContentAsString();
        JWTResponseDTO responseDTO = om.readValue(resultContent, JWTResponseDTO.class);
        jwtToken = jwtToken + responseDTO.getToken();
        Assert.assertNotNull(responseDTO.getToken());
    }

    @Test(expected = Exception.class)
    public void TestUserSignIn_InvalidCredentials() throws Exception{
        LoginDTO login = new LoginDTO();
        login.setPassword("testUse");
        login.setUserName("TestUser");
        String jsonRequest = om.writeValueAsString(login);
        MvcResult result = mockMvc.perform(post("/user/signin").content(jsonRequest).contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isUnauthorized()).andReturn();
    }

    @Test
    public void TestUserSignIn_DeleteUser() throws Exception {
        UserDTO userDTO = new UserDTO("TestUserDelete", "testUserDelete", "testUserDelete@gmail.com", "233123321", "USER");
        String jsonRequest = om.writeValueAsString(userDTO);
        MvcResult createResult = mockMvc.perform(post("/user/signup").content(jsonRequest).contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk()).andReturn();
        MvcResult result = mockMvc.perform(delete("/gps/delete/user").header(AUTH_HEADER, jwtToken).param("userName", "TestUserDelete")).andExpect(status().isOk()).andReturn();
        String resultContent = result.getResponse().getContentAsString();
        ServiceResponseDTO responseDTO = om.readValue(resultContent, ServiceResponseDTO.class);
        Assert.assertEquals("SUCCESS", responseDTO.getStatus());
    }

    @Test
    public void TestUserSignInCreateVM() throws Exception{
        VMProvisionRequest request = new VMProvisionRequest("Mac OS", 32, 528, 16);
        String jsonRequest = om.writeValueAsString(request);
        MvcResult result = mockMvc.perform(post("/gps/provisionService/requestVM").content(jsonRequest).contentType(MediaType.APPLICATION_JSON_VALUE).header(AUTH_HEADER, jwtToken)).andExpect(status().isOk()).andReturn();
        String resultContent = result.getResponse().getContentAsString();
        ServiceResponseDTO responseDTO = om.readValue(resultContent, ServiceResponseDTO.class);
        Assert.assertEquals("SUCCESS", responseDTO.getStatus());
    }

    @Test
    public void TestUserSignInGetVMForUser() throws Exception{
        MvcResult result = mockMvc.perform(get("/gps/provisionService/getVMsForUser").header(AUTH_HEADER, jwtToken).param("userName", "TestUser")).andExpect(status().isOk()).andReturn();
        String resultContent = result.getResponse().getContentAsString();
        List<VMDetailsResponse> responseDTO = Arrays.asList(om.readValue(resultContent, VMDetailsResponse[].class));
        Assert.assertEquals(1, responseDTO.size());
    }

    @Test
    public void TestUserSignInGetVM_BasedOnRAM() throws Exception{
        //Create vm for testuser
        VMProvisionRequest request = new VMProvisionRequest("Mac OS", 32, 528, 16);
        String jsonRequest = om.writeValueAsString(request);
        MvcResult result = mockMvc.perform(post("/gps/provisionService/requestVM").content(jsonRequest).contentType(MediaType.APPLICATION_JSON_VALUE).header(AUTH_HEADER, jwtToken)).andExpect(status().isOk()).andReturn();

        VMProvisionRequest userRequest = new VMProvisionRequest("Mac OS", 64, 528, 16);
        String userJsonRequest = om.writeValueAsString(userRequest);
        MvcResult userResult = mockMvc.perform(post("/gps/provisionService/requestVM").content(userJsonRequest).contentType(MediaType.APPLICATION_JSON_VALUE).header(AUTH_HEADER, jwtToken)).andExpect(status().isOk()).andReturn();

        //Get vm based on RAM across all users
        MvcResult getResult = mockMvc.perform(get("/gps/provisionService/getTopN_VMsBasedOnRAM").header(AUTH_HEADER, jwtToken).param("count", "100")).andExpect(status().isOk()).andReturn();
        String getResultContent = getResult.getResponse().getContentAsString();
        List<VMDetailsResponse> responseDTO1 = Arrays.asList(om.readValue(getResultContent, VMDetailsResponse[].class));
        Assert.assertEquals(32, responseDTO1.get(0).getRam());
    }

    @Test
    public void TestUserSignInGetVM_BasedOnRAMAndUser() throws Exception{
        //Create vm for testuser
        VMProvisionRequest request = new VMProvisionRequest("Mac OS", 32, 528, 16);
        String jsonRequest = om.writeValueAsString(request);
        MvcResult result = mockMvc.perform(post("/gps/provisionService/requestVM").content(jsonRequest).contentType(MediaType.APPLICATION_JSON_VALUE).header(AUTH_HEADER, jwtToken)).andExpect(status().isOk()).andReturn();

        VMProvisionRequest userRequest = new VMProvisionRequest("Mac OS", 64, 528, 16);
        String userJsonRequest = om.writeValueAsString(userRequest);
        MvcResult userResult = mockMvc.perform(post("/gps/provisionService/requestVM").content(userJsonRequest).contentType(MediaType.APPLICATION_JSON_VALUE).header(AUTH_HEADER, jwtToken)).andExpect(status().isOk()).andReturn();

        //Get vm based on RAM across all users
        MvcResult getResult = mockMvc.perform(get("/gps/provisionService/getTopN_VMsBasedOnRAM").header(AUTH_HEADER, jwtToken).param("count", "100").param("userName", "TestUser")).andExpect(status().isOk()).andReturn();
        String getResultContent = getResult.getResponse().getContentAsString();
        List<VMDetailsResponse> responseDTO1 = Arrays.asList(om.readValue(getResultContent, VMDetailsResponse[].class));
        Assert.assertEquals(32, responseDTO1.get(0).getRam());
    }

    @Test
    public void TestUserSignIn_SelfDelete() throws Exception {
        MvcResult result = mockMvc.perform(delete("/gps/delete/self").header(AUTH_HEADER, jwtToken)).andExpect(status().isOk()).andReturn();
        String resultContent = result.getResponse().getContentAsString();
        ServiceResponseDTO responseDTO = om.readValue(resultContent, ServiceResponseDTO.class);
        Assert.assertEquals("SUCCESS", responseDTO.getStatus());
    }
}
