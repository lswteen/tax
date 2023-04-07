package com.jobis.tax.application.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobis.tax.application.request.SignInRequest;
import com.jobis.tax.application.request.SignUpRequest;
import com.jobis.tax.application.request.TokenRequest;
import com.jobis.tax.application.response.TokenResponse;
import com.jobis.tax.core.exception.ApiException;
import com.jobis.tax.core.type.ServiceErrorType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ExtendWith(SpringExtension.class)
class AuthControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Test
    void signUp_success() throws Exception {
        SignUpRequest signUpRequest = new SignUpRequest("마징가",
                "dev", "abcde가A1!", "01012345678", "ma@gmail.com","8806012455116", "MALE");

        mockMvc.perform(post("/szs/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("name").exists())
                .andExpect(jsonPath("nickname").exists())
                .andExpect(jsonPath("phoneNumber").exists())
                .andExpect(jsonPath("userId").exists())
                .andExpect(jsonPath("regNo").exists())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    @Test
    void testInvalidRegNo() {
        SignUpRequest signUpRequest = new SignUpRequest(
                "제임스",
                "james",
                "P@ssw0rd123!",
                "01012345678",
                "james@gmail.com",
                "1234567890123", // Invalid 주민번호
                "MALE"
        );

        ApiException exception = assertThrows(
                ApiException.class,
                signUpRequest::validation,
                "허용된 주민등록번호가 아닙니다."
        );

        assertSame(ServiceErrorType.INVALID_USER_REG_NO.getMessage(), exception.getMessage(),
                "허용된 주민등록번호가 아닙니다.");
    }


    private MvcResult signIn() throws Exception {
        SignInRequest signInRequest = new SignInRequest("hong@gmail.com", "abcde가A1!");

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
        SignInRequest signInRequest = new SignInRequest("hong@gmail.com", "ww!");

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

    @Test
    void refresh_success() throws Exception {
        String content = signIn().getResponse().getContentAsString();
        TokenResponse tokenResponse = objectMapper.readValue(content, TokenResponse.class);

        TokenRequest tokenRequest = new TokenRequest(tokenResponse.getAccessToken(), tokenResponse.getRefreshToken());

        mockMvc.perform(post("/szs/reissuance")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tokenRequest)))
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
    void signout_success() throws Exception {
        String content = signIn().getResponse().getContentAsString();
        TokenResponse tokenResponse = objectMapper.readValue(content, TokenResponse.class);

        TokenRequest tokenRequest = new TokenRequest(tokenResponse.getAccessToken(), tokenResponse.getRefreshToken());

        mockMvc.perform(delete("/szs/signout")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenRequest.getAccessToken()))
                .andDo(print())
                .andExpect(status().isOk())
        ;
    }

}