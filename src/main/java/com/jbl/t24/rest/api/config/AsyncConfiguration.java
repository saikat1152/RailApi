package com.jbl.t24.rest.api.config;

import java.util.concurrent.Executor;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.lang.Nullable;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;



@Configuration
@EnableAsync
public class AsyncConfiguration extends AsyncConfigurerSupport {
	
	// @Bean
    public Executor getAsyncExecutor() {
        // ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // return executor;
        return new SimpleAsyncTaskExecutor();
    }

    @Override
    @Nullable
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        
        return (throwable, method, obj) -> {

            System.out.println("Exception Caught in Thread - "+ Thread.currentThread().getName());
            System.out.println("Exception message - "+throwable.getMessage());
            System.out.println("Method name - "+method.getName());

            for(Object param: obj){
                System.out.println("Parameter value- "+param);
            }

        };


        // return super.getAsyncUncaughtExceptionHandler();
    }

}
