package com.jobis.tax.application.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobis.tax.application.request.SignInRequest;
import com.jobis.tax.application.request.SignUpRequest;
import com.jobis.tax.domain.scrap.external.SzsApiClient;
import com.jobis.tax.domain.scrap.service.ScrapService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Test
    void signUp_success() throws Exception {
        SignUpRequest signUpRequest = new SignUpRequest("테스트",
                "dev", "abcde가A1!", "01012345678", "renzo@gmail.com","8012251231231", "MALE");

        mockMvc.perform(post("/szs/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("name").exists())
                .andExpect(jsonPath("nickname").exists())
                .andExpect(jsonPath("phoneNumber").exists())
                .andExpect(jsonPath("email").exists())
                .andExpect(jsonPath("regNo").exists())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }


    private MvcResult signIn() throws Exception {
        SignInRequest signInRequest = new SignInRequest("user@gmail.com", "abcde가A1!");

        return mockMvc.perform(post("/szs/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signInRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("grantType").exists())
                .andExpect(jsonPath("accessToken").exists())
                .andExpect(jsonPath("accessTokenExpiresIn").exists())
                .andExpect(jsonPath("refreshToken").exists())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    @Test
    void signIn_unauthorized() throws Exception {
        SignInRequest signInRequest = new SignInRequest("user@gmail.com", "ww!");

        mockMvc.perform(post("/szs/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signInRequest)))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    void signIn_success() throws Exception {
        signIn();
    }

}