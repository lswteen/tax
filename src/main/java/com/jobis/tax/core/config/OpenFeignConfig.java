package com.jobis.tax.core.config;

import feign.Retryer;
import feign.codec.ErrorDecoder;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignFormatterRegistrar;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;

import static java.lang.String.format;

@Configuration
@EnableFeignClients("com.jobis.tax.domain")
public class OpenFeignConfig {
    @Bean
    FeignFormatterRegistrar dateTimeFormatterRegistrar() {
        return registry -> {
            DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
            registrar.setUseIsoFormat(true);
            registrar.registerFormatters(registry);
        };
    }

    /**
     * 재시도는 100ms를 시작으로 최대 1초 까지 재시도 하고, 최대 3번으로 재시도 하도록 설정
     * 최초 100ms이후, 1.5를 곱하면서 재시도
     * @return
     */
    @Bean
    Retryer retryer() {
        return new Retryer.Default(20000, 1_000, 3);
    }

    @Bean
    ErrorDecoder decoder() {
        return (methodKey, response) -> {
//            if (HttpStatus.INTERNAL_SERVER_ERROR.contains(response.status())) {
//                return new RetryableException(format("%s 요청이 성공하지 못했습니다. Retry 합니다. - status: %s, headers: %s", methodKey, response.status(), response.headers()), null);
//            }

            return new IllegalStateException(format("%s 요청이 성공하지 못했습니다. - status: %s, headers: %s", methodKey, response.status(), response.headers()));
        };
    }
}
