package com.jobis.tax.application.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobis.tax.core.response.ApiErrorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setContentType( MediaType.APPLICATION_JSON_VALUE );

        ApiErrorResponse apiErrorResponse = new ApiErrorResponse();
        apiErrorResponse.setErrorMessage(accessDeniedException.getMessage());
        apiErrorResponse.setErrorCode(HttpServletResponse.SC_FORBIDDEN);

        response.sendError(HttpServletResponse.SC_FORBIDDEN, objectMapper.writeValueAsString(apiErrorResponse));
    }
}
