package com.jobis.tax.application.service;

import com.jobis.tax.application.request.SignInRequest;
import com.jobis.tax.application.request.SignUpRequest;
import com.jobis.tax.application.request.TokenRequest;
import com.jobis.tax.application.response.TokenResponse;
import com.jobis.tax.application.response.UserResponse;
import com.jobis.tax.application.security.dto.PrincipalDetails;
import com.jobis.tax.application.security.provider.TokenProvider;
import com.jobis.tax.core.exception.ApiException;
import com.jobis.tax.core.type.ServiceErrorType;
import com.jobis.tax.domain.user.entity.RefreshToken;
import com.jobis.tax.domain.user.entity.User;
import com.jobis.tax.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserAppService {
    private final UserService userService;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public TokenResponse signIn(SignInRequest signInRequest) {
        // 1. ID/PW 로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(signInRequest.getEmail(), signInRequest.getPassword());

        // 2. 사용자 검증
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3.JWT 토큰 생성
        TokenResponse customToken = tokenProvider.generateToken(authentication);

        // 4. RefreshToken 저장
        userService.createRefreshToken(authentication, customToken.getRefreshToken());

        // 5. 토큰 발급
        return customToken;
    }

    @Transactional
    public TokenResponse reissuance(TokenRequest tokenRequest) {
        // 1. Refresh Token 검증
        if (!tokenProvider.validateToken(tokenRequest.getRefreshToken())) {
            // 1-1 토큰 삭제
            userService.deleteByRefreshToken(tokenRequest.getRefreshToken());

            throw new ApiException(ServiceErrorType.INVALID_USER_REFRESH_TOKEN);
        }

        // 2. Access Token 에서 인증 정보
        Authentication authentication = tokenProvider.getAuthentication(tokenRequest.getAccessToken());

        // 3. Refresh Token 정보
        RefreshToken refreshToken = userService.getByRefreshToken(tokenRequest.getRefreshToken());

        // 5. 새로운 토큰 생성
        TokenResponse customToken = tokenProvider.generateAccessToken(authentication);

        customToken.setRefreshToken(refreshToken.getToken());

        // 토큰 발급
        return customToken;
    }

    @Transactional
    public UserResponse signUp(SignUpRequest signUpRequest) {
        signUpRequest.validation();

        User user = User.signUpBuilder()
                .email(signUpRequest.getEmail())
                .name(signUpRequest.getName())
                .nickname(signUpRequest.getNickname())
                .password(passwordEncoder.encode(signUpRequest.getPassword()))
                .phoneNumber(signUpRequest.getPhoneNumber())
                .gender(signUpRequest.getGender())
                .build();

        User newUser = userService.save(user);

        return UserResponse.builder()
                .id(newUser.getId())
                .gender(newUser.getGenderAsString())
                .email(newUser.getEmail())
                .name(newUser.getName())
                .nickname(newUser.getNickname())
                .phoneNumber(newUser.getPhoneNumber())
                .createdAt(newUser.getCreatedAt())
                .updatedAt(newUser.getUpdatedAt())
                .build();
    }

    @Transactional
    public void signOut(PrincipalDetails principal) {
        userService.deleteByUserId(principal.getUser().getId());
    }

    public UserResponse me(PrincipalDetails principal) {
        User user = userService.getById(principal.getUser().getId());

        return UserResponse.builder()
                .id(user.getId())
                .gender(user.getGenderAsString())
                .email(user.getEmail())
                .name(user.getName())
                .nickname(user.getNickname())
                .phoneNumber(user.getPhoneNumber())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
