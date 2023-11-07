package dev.soon.interviewdefense.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;

@OpenAPIDefinition(info = @Info(title = "Tech Mentor API 명세서"))
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi chatOpenApi() {
        return GroupedOpenApi.builder()
                .group("Tech Mentor Rest API")
                .pathsToMatch("/**")
                .build();
    }
}
