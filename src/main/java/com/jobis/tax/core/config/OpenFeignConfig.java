package com.jobis.tax.core.config;

import feign.Request;
import feign.Retryer;
import feign.codec.ErrorDecoder;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClientBuilder;
import org.springframework.cloud.openfeign.FeignFormatterRegistrar;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;

import java.util.concurrent.TimeUnit;

import static java.lang.String.format;

@Configuration
@EnableFeignClients("com.jobis.tax")
public class OpenFeignConfig {
    @Bean
    FeignFormatterRegistrar dateTimeFormatterRegistrar() {
        return registry -> {
            DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
            registrar.setUseIsoFormat(true);
            registrar.registerFormatters(registry);
        };
    }


    @Bean
    public Request.Options requestOptions() {
        return new Request.Options(20_000, TimeUnit.MILLISECONDS, 20_000, TimeUnit.MILLISECONDS, true);
    }


    /**
     * 재시도는 20000ms를 시작으로 최대 1초 까지 재시도 하고, 최대 3번으로 재시도 하도록 설정
     * 최초 20000ms이후, 1.5를 곱하면서 재시도
     * @return
     */
    @Bean
    Retryer retryer() {
        return new Retryer.Default(20000, 20_000, 3);
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
