package com.jobis.tax.application.controller;

import com.jobis.tax.application.response.UserResponse;
import com.jobis.tax.application.security.dto.PrincipalDetails;
import com.jobis.tax.application.service.UserAppService;
import com.jobis.tax.domain.scrap.entity.TaxInformation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "User", description = "User 기능을 제공합니다.")
public class UserController {

    private final UserAppService userAppService;

    @Operation(
            summary = "회원 정보",
            description = "회원정보 기능을 제공합니다."
    )
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/szs/me")
    public UserResponse me(@Parameter(hidden = true) @AuthenticationPrincipal PrincipalDetails principal) {
        return userAppService.me(principal);
    }

    @Operation(
            summary = "스크랩",
            description = "회원 토큰정보 정보 스크랩 기능을 제공합니다."
    )
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("/szs/scrap")
    public TaxInformation scrap(@Parameter(hidden = true) @AuthenticationPrincipal PrincipalDetails principal){
        return userAppService.scrap(principal);
    }
}
