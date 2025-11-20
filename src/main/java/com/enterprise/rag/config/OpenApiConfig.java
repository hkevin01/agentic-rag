package com.enterprise.rag.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for OpenAPI/Swagger documentation.
 */
@Configuration
public class OpenApiConfig {

  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
        .info(new Info()
            .title("Agentic-RAG API")
            .version("0.1.0")
            .description(
                "Enterprise-grade Retrieval-Augmented Generation system with autonomous agent capabilities. "
                    + "This API provides intelligent chat functionality with document retrieval, "
                    + "agent-based orchestration, and multi-step reasoning."
            )
            .contact(new Contact()
                .name("Agentic-RAG Team")
                .url("https://github.com/yourusername/agentic-rag")
            )
            .license(new License()
                .name("MIT License")
                .url("https://opensource.org/licenses/MIT")
            )
        );
  }
}
