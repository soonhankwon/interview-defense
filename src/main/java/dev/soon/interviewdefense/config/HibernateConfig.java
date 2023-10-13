package dev.soon.interviewdefense.config;

import dev.soon.interviewdefense.log.interceptor.MDCRequestHandlerCommentInterceptor;
import org.hibernate.cfg.Environment;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HibernateConfig {

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer() {
        return hibernateProperties -> hibernateProperties.put(Environment.INTERCEPTOR, new MDCRequestHandlerCommentInterceptor());
    }
}
