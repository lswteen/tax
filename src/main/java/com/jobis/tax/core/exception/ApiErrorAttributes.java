package com.jobis.tax.core.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobis.tax.core.response.ApiErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.RequestDispatcher;
import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class ApiErrorAttributes {

    private final ObjectMapper objectMapper;

    @Bean
    public DefaultErrorAttributes errorAttributes() {
        return new DefaultErrorAttributes() {
            @Override
            public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
                String message = super.getMessage(webRequest, super.getError(webRequest));
                Integer status = this.getAttribute(webRequest, RequestDispatcher.ERROR_STATUS_CODE);

                if (status == null) {
                    status = 999;
                }

                ApiErrorResponse apiErrorResponse = new ApiErrorResponse();
                apiErrorResponse.setErrorMessage(message);
                apiErrorResponse.setErrorCode(status);

                return objectMapper.convertValue(apiErrorResponse, Map.class);
            }

            private <T> T getAttribute(RequestAttributes requestAttributes, String name) {
                return (T) requestAttributes.getAttribute(name, RequestAttributes.SCOPE_REQUEST);
            }
        };
    }
}
