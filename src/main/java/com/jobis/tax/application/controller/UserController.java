package com.jobis.tax.application.controller;

import com.jobis.tax.application.service.UserAppService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "01.Test")
public class UserController {

    private final UserAppService userAppService;

    @GetMapping("/test")
    public String hello(String abc){
        return "abc";
    }
}
