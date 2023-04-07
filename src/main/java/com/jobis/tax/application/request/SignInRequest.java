package com.jobis.tax.application.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SignInRequest {
    @NotNull(message = "아이디는 필수 항목입니다.")
    private String userId;
    @NotNull(message = "패스워드는 필수 항목입니다.")
    private String password;
}
