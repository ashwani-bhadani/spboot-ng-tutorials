package com.tutorial.security.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean("asyncMailSender")
    public Executor asyncTaskExecutor() {
        //you need from scheduling package not util.concurrent, the underlying executor is still a java.util.concurrent.ThreadPoolExecutor
        ThreadPoolTaskExecutor mailSenderTask = new ThreadPoolTaskExecutor();
        mailSenderTask.setCorePoolSize(4);
        mailSenderTask.setQueueCapacity(150);
        mailSenderTask.setMaxPoolSize(4);
        mailSenderTask.setThreadNamePrefix("Async-mail-sender-");
        mailSenderTask.initialize();

        //when core pool is full, queue full, max pool size reached then thread pool rejects the task and delegates the decision to a RejectedExecutionHandler
        //Setting RejectedExecutionHandler for ThreadPoolTaskExecutor should be done only after executor is initialized
        mailSenderTask.getThreadPoolExecutor().setRejectedExecutionHandler(
                new ThreadPoolExecutor.AbortPolicy() //this policy simply throws a RejectedExecutionException and does nothing else.
        );
        //CallerRunsPolicy: If a task cannot be accepted, it is run by the thread that submitted the task (the caller thread),
        // instead of being silently dropped or causing an exception, prevents loss, applies backpressure to producer
        //others: DiscardPolicy & DiscardOldestPolicy

        return mailSenderTask;
    }

}
