package dev.soon.interviewdefense.config;

import com.theokanning.openai.service.OpenAiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Slf4j
@Configuration
public class ChatGPTConfig {

    @Value("${chatgpt.api-key}")
    private String token;

    @Bean
    public OpenAiService openAiService() {
        return new OpenAiService(token, Duration.ofSeconds(60));
    }
}
