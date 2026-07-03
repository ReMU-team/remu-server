package com.remu.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean(name = "externalApiExecutor")
    public Executor externalApiExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(200);
        executor.setQueueCapacity(10); // 큐를 작게 잡아 대기가 밀리면 빠르게 새 스레드 생성 유도
        executor.setThreadNamePrefix("ExtAPI-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy()); // 과부하 시 예외 발생
        executor.initialize();
        return executor;
    }
}
