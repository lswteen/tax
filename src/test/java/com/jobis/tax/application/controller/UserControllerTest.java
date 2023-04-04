package com.jobis.tax.application.controller;

import com.jobis.tax.application.response.TokenResponse;
import com.jobis.tax.application.security.provider.TokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    @Autowired
    private AuthenticationManagerBuilder authenticationManagerBuilder;
    @Autowired
    private TokenProvider tokenProvider;
    @Autowired
    protected MockMvc mockMvc;

    private TokenResponse tokenResponse;

    @BeforeEach
    void setUp() {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken("user@gmail.com", "abcde가A1!");

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        tokenResponse = tokenProvider.generateToken(authentication);
    }

    @Test
    void me() throws Exception {
        mockMvc.perform(get("/szs/me")
                        .header(HttpHeaders.AUTHORIZATION, "bearer " + tokenResponse.getAccessToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("name").exists())
                .andExpect(jsonPath("nickname").exists())
                .andExpect(jsonPath("phoneNumber").exists())
                .andExpect(jsonPath("email").exists())
        ;
    }

}