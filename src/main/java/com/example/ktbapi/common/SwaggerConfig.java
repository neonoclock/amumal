package com.example.ktbapi.common;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI()
                .info(new Info()
                        .title("아무말 대잔치 API 문서")
                        .description("아무말 대잔치 API 문서입니다.")
                        .version("1.0.0"));
    }
}
