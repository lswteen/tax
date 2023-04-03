package com.jobis.tax.application.controller;

import com.jobis.tax.application.service.UserAppService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final UserAppService userAppService;

//    @ApiOperation(value = "로그인",
//            produces = MediaType.APPLICATION_JSON_VALUE,
//            responseHeaders = {
//                    @ResponseHeader(name = HttpHeaders.CONTENT_TYPE, description = MediaType.APPLICATION_JSON_VALUE)
//            })
//    @PostMapping("/szs/login")
//    public TokenResponse signIn(@Valid @RequestBody SignInRequest signInRequest) {
//        return userAppService.signIn(signInRequest);
//    }

//    @ApiOperation(value = "토큰 재발행",
//            produces = MediaType.APPLICATION_JSON_VALUE,
//            responseHeaders = {
//                    @ResponseHeader(name = HttpHeaders.CONTENT_TYPE, description = MediaType.APPLICATION_JSON_VALUE)
//            })
//    @PostMapping("/szs/reissuance")
//    public TokenResponse reissuance(@Valid @RequestBody TokenRequest tokenRequest) {
//        return userAppService.reissuance(tokenRequest);
//    }
//
//    @ApiOperation(value = "회원 가입",
//            produces = MediaType.APPLICATION_JSON_VALUE,
//            responseHeaders = {
//                    @ResponseHeader(name = HttpHeaders.CONTENT_TYPE, description = MediaType.APPLICATION_JSON_VALUE)
//            })
//    @PostMapping("/szs/signup")
//    public UserResponse signUp(@RequestBody SignUpRequest signUpRequest) {
//        return userAppService.signUp(signUpRequest);
//    }
//
//    @ApiOperation(value = "로그아웃",
//            produces = MediaType.APPLICATION_JSON_VALUE,
//            responseHeaders = {
//                    @ResponseHeader(name = HttpHeaders.CONTENT_TYPE, description = MediaType.APPLICATION_JSON_VALUE),
//                    @ResponseHeader(name = HttpHeaders.AUTHORIZATION, description = "bearer token")
//            })
//    @DeleteMapping("/szs/signout")
//    public void signOut(@ApiIgnore @AuthenticationPrincipal PrincipalDetails principal) {
//        userAppService.signOut(principal);
//    }

}
