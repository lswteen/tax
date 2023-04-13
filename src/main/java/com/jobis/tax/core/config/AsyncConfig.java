package com.jobis.tax.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

@EnableAsync
@Configuration
public class AsyncConfig {
    @Bean
    TaskExecutor taskExecutor(){
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(10);     // 기본 생성되는 스레드 수
        threadPoolTaskExecutor.setMaxPoolSize(20);      // 최대 스레드 수
        threadPoolTaskExecutor.setQueueCapacity(5);     // 대기 Queue 사이즈
        threadPoolTaskExecutor.setThreadNamePrefix("event-");   // thread 동작시 실행하는 thread 이름의 prefix
        // thread 생성이 거부되는 경우 처리 방식
        // Thread생성이 Reject되는 경우, 호출한 Thread(working thread)에서 대신 실행됨
        threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return threadPoolTaskExecutor;
    }
}
