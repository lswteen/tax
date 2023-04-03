package com.jobis.tax.application.controller;

import com.jobis.tax.application.response.UserResponse;
import com.jobis.tax.application.security.dto.PrincipalDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/szs")
public class UserController {
    public UserResponse me(@AuthenticationPrincipal PrincipalDetails principal){
        return null;
    }
}
