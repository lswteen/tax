package com.jobis.tax.application.security.entrypoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobis.tax.core.response.ApiErrorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        response.setContentType( MediaType.APPLICATION_JSON_VALUE );

        ApiErrorResponse apiErrorResponse = new ApiErrorResponse();
        apiErrorResponse.setErrorMessage(authException.getMessage());
        apiErrorResponse.setErrorCode(HttpServletResponse.SC_UNAUTHORIZED);

        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, objectMapper.writeValueAsString(apiErrorResponse));
    }
}
