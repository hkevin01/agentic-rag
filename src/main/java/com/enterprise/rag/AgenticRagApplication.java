package com.enterprise.rag;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Main Spring Boot application class for Agentic-RAG.
 *
 * <p>This application provides an enterprise-grade Retrieval-Augmented Generation (RAG) system
 * enhanced with autonomous agent capabilities. The system combines traditional RAG with
 * intelligent agents that can plan, reason, use tools, and orchestrate complex multi-step
 * workflows.
 *
 * <p>Key Features:
 * <ul>
 *   <li>Autonomous planning and task decomposition</li>
 *   <li>Hybrid search (vector + keyword) with reranking</li>
 *   <li>Multi-agent collaboration and tool usage</li>
 *   <li>Conversation memory and context management</li>
 *   <li>Resilient architecture with circuit breakers and retries</li>
 * </ul>
 *
 * @author Agentic-RAG Team
 * @version 0.1.0
 * @since 2025-11-19
 */
@SpringBootApplication
@EnableCaching
@EnableJpaRepositories
public class AgenticRagApplication {

  /**
   * Main entry point for the Spring Boot application.
   *
   * @param args command-line arguments passed to the application
   */
  public static void main(String[] args) {
    SpringApplication.run(AgenticRagApplication.class, args);
  }
}
