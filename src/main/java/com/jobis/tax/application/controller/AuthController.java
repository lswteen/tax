package com.jobis.tax.application.controller;

import com.jobis.tax.application.request.SignInRequest;
import com.jobis.tax.application.request.SignUpRequest;
import com.jobis.tax.application.request.TokenRequest;
import com.jobis.tax.application.response.TokenResponse;
import com.jobis.tax.application.response.UserResponse;
import com.jobis.tax.application.security.dto.PrincipalDetails;
import com.jobis.tax.application.service.UserAppService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Auth 기능을 제공합니다.")
public class AuthController {
    private final UserAppService userAppService;

    @Operation(
            summary = "로그인",
            description = "회원정보 기능을 제공합니다."
    )
    @PostMapping("/szs/login")
    public TokenResponse signIn(@Valid @RequestBody SignInRequest signInRequest) {
        return userAppService.signIn(signInRequest);
    }

    @Operation(
            summary = "토큰 재발행",
            description = "토큰 재발행  기능을 제공합니다."
    )
    @PostMapping("/szs/reissuance")
    public TokenResponse reissuance(@Valid @RequestBody TokenRequest tokenRequest) {
        return userAppService.reissuance(tokenRequest);
    }

    @Operation(
            summary = "회원 가입",
            description = "회원 가입 기능을 제공합니다."
    )
    @PostMapping("/szs/signup")
    public UserResponse signUp(@RequestBody SignUpRequest signUpRequest) {
        return userAppService.signUp(signUpRequest);
    }


    @Operation(
            summary = "로그아웃",
            description = "로그아웃 기능을 제공합니다."
    )
    @SecurityRequirement(name = "Bearer Authentication")
    @DeleteMapping("/szs/signout")
    public void signOut(@Parameter(hidden = true) @AuthenticationPrincipal PrincipalDetails principal) {
        userAppService.signOut(principal);
    }

}
