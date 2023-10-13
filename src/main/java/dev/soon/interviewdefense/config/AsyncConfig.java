package dev.soon.interviewdefense.config;

import dev.soon.interviewdefense.log.decorator.MDCCopyTaskDecorator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig extends AsyncConfigurerSupport {

    @Override
    public Executor getAsyncExecutor() {
        return asyncThreadPoolTaskExecutor();
    }

    @Bean
    public ThreadPoolTaskExecutor asyncThreadPoolTaskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(5);
        taskExecutor.setMaxPoolSize(30);
        taskExecutor.setQueueCapacity(50);
        taskExecutor.setTaskDecorator(new MDCCopyTaskDecorator());
        taskExecutor.initialize();
        return taskExecutor;
    }
}
