package com.jobis.tax.domain.user.service;


import com.jobis.tax.application.security.dto.PrincipalDetails;
import com.jobis.tax.core.exception.ApiException;
import com.jobis.tax.core.type.ServiceErrorType;
import com.jobis.tax.domain.user.entity.RefreshToken;
import com.jobis.tax.domain.user.entity.User;
import com.jobis.tax.domain.user.repository.RefreshTokenRepository;
import com.jobis.tax.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    public User getByEmail(String userId) {
        return userRepository.findByEmail(userId)
                .orElseThrow(() -> new ApiException(ServiceErrorType.NOT_FOUND));
    }

    public User getById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ServiceErrorType.NOT_FOUND));
    }

    public void createRefreshToken(Authentication authentication, String token) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = this.getById(((PrincipalDetails) userDetails).getUser().getId());

        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(token)
                .build();
        refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken getByRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new ApiException(ServiceErrorType.WAS_LOGOUT_USER));
    }

    public void deleteByRefreshToken(String refreshToken) {
        refreshTokenRepository.deleteByToken(refreshToken);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public void deleteByUserId(Long id) {
        User user = this.getById(id);

        List<RefreshToken> refreshTokens = user.getRefreshTokens();

        user.setRefreshTokens(new ArrayList<>());

        this.save(user);
        refreshTokenRepository.deleteAll(refreshTokens);
    }

    public Page<User> getSearchByPageable(String name, String email, Pageable pageable) {
        Page<User> user;

        if (Objects.nonNull(name) && Objects.nonNull(email)) {
            user = userRepository.findByNameContainingOrEmailContaining(name, email, pageable);
        } else if (Objects.nonNull(email)) {
            user = userRepository.findByEmailContaining(email, pageable);
        } else if (Objects.nonNull(name)) {
            user = userRepository.findByNameContaining(name, pageable);
        } else {
            user = userRepository.findAll(pageable);
        }

        return user;
    }
}
