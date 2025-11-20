package com.enterprise.rag.api;

import com.enterprise.rag.dto.ChatRequest;
import com.enterprise.rag.dto.ChatResponse;
import com.enterprise.rag.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for chat operations.
 * Provides endpoints for interacting with the Agentic-RAG system.
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Chat", description = "Chat API for Agentic-RAG system")
public class ChatController {

  private final ChatService chatService;

  /**
   * Chat endpoint for user queries.
   *
   * @param request the chat request
   * @return chat response with answer and metadata
   */
  @PostMapping("/chat")
  @Operation(
      summary = "Chat with the RAG system",
      description = "Submit a query and receive an AI-generated response grounded in the knowledge base"
  )
  public ResponseEntity<ChatResponse> chat(@Valid @RequestBody ChatRequest request) {
    log.info("Received chat request for session: {}", request.getSessionId());
    ChatResponse response = chatService.chat(request);
    return ResponseEntity.ok(response);
  }

  /**
   * Health check endpoint.
   */
  @GetMapping("/health")
  @Operation(summary = "Health check", description = "Check if the API is operational")
  public ResponseEntity<String> health() {
    return ResponseEntity.ok("Agentic-RAG API is healthy");
  }
}
