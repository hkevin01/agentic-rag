package com.enterprise.rag.service;

import com.enterprise.rag.domain.Conversation;
import com.enterprise.rag.domain.Message;
import com.enterprise.rag.dto.ChatRequest;
import com.enterprise.rag.dto.ChatResponse;
import com.enterprise.rag.exception.ResourceNotFoundException;
import com.enterprise.rag.exception.ServiceException;
import com.enterprise.rag.repository.ConversationRepository;
import com.enterprise.rag.repository.MessageRepository;
import com.enterprise.rag.util.ErrorHandler;
import com.enterprise.rag.util.TimeUtils;
import com.enterprise.rag.util.ValidationUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Service for handling chat operations with RAG capabilities.
 * Orchestrates conversation management, retrieval, and LLM interaction.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ChatService {

  private final ConversationRepository conversationRepository;
  private final MessageRepository messageRepository;

  /**
   * Process a chat request and generate a response.
   * Handles conversation creation, message persistence, and response generation.
   *
   * REQ-SVC-001: Validate all inputs before processing
   * REQ-SVC-002: Log all operations with context
   * REQ-SVC-003: Handle errors gracefully and return meaningful responses
   *
   * @param request the chat request
   * @return chat response
   * @throws IllegalArgumentException if request validation fails
   * @throws ServiceException if chat processing fails
   */
  @Transactional
  public ChatResponse chat(ChatRequest request) {
    // REQ-001: Validate input parameters
    validateChatRequest(request);

    log.info("Processing chat request for session: {} (user: {})",
        request.getSessionId(), request.getUserId());

    long startTime = System.currentTimeMillis();

    try {
      // Get or create conversation
      Conversation conversation = getOrCreateConversation(
          request.getSessionId(), request.getUserId());

      // Save user message
      Message userMessage = saveMessage(conversation, "user", request.getQuery());

      // Generate response (mocked for now)
      String answer = generateMockResponse(request.getQuery());

      // Save assistant message
      Message assistantMessage = saveMessage(conversation, "assistant", answer);

      // Calculate processing time
      long processingTime = System.currentTimeMillis() - startTime;
      log.info("Chat request processed in {}ms", processingTime);

      // Build response
      return ChatResponse.builder()
          .answer(answer)
          .sessionId(request.getSessionId())
          .conversationId(conversation.getId().toString())
          .sources(Collections.emptyList())
          .metadata(buildMetadata(conversation, processingTime))
          .timestamp(LocalDateTime.now())
          .tokensUsed(estimateTokens(request.getQuery(), answer))
          .model("gpt-4-mock")
          .build();

    } catch (IllegalArgumentException e) {
      // REQ-ERR-003: Re-throw validation errors
      throw e;
    } catch (Exception e) {
      // REQ-ERR-001: Log error with context
      Map<String, Object> errorContext = ErrorHandler.createErrorContext(
          "ChatService.chat", request);
      ErrorHandler.logError("ChatService.chat",
          "Failed to process chat request", e, errorContext);

      // REQ-ERR-003: Return user-friendly error
      throw new ServiceException("Failed to process chat request: " +
          ErrorHandler.getUserFriendlyMessage(e), e);
    }
  }

  /**
   * Validate chat request inputs.
   * REQ-001: Validate all user inputs for null, empty, and boundary conditions.
   *
   * @param request the chat request to validate
   * @throws IllegalArgumentException if validation fails
   */
  private void validateChatRequest(ChatRequest request) {
    ValidationUtils.requireNonNull(request, "request");
    ValidationUtils.requireNonEmpty(request.getQuery(), "query");
    ValidationUtils.requireNonEmpty(request.getSessionId(), "sessionId");
    ValidationUtils.validateStringLength(request.getQuery(), "query", 4000);

    if (request.getTemperature() != null) {
      ValidationUtils.validateTemperature(request.getTemperature());
    }

    if (request.getMaxTokens() != null) {
      ValidationUtils.validateTokenCount(request.getMaxTokens());
    }
  }

  /**
   * Get conversation by session ID or create new one.
   */
  private Conversation getOrCreateConversation(String sessionId, String userId) {
    try {
      UUID conversationId = UUID.fromString(sessionId);
      return conversationRepository.findById(conversationId)
          .orElseGet(() -> createConversation(userId));
    } catch (IllegalArgumentException e) {
      return createConversation(userId);
    }
  }

  /**
   * Create a new conversation.
   */
  private Conversation createConversation(String userId) {
    Conversation conversation = Conversation.builder()
        .userId(userId)
        .title("New Conversation")
        .status("active")
        .metadata(new HashMap<>())
        .build();
    return conversationRepository.save(conversation);
  }

  /**
   * Save a message to the conversation.
   */
  private Message saveMessage(Conversation conversation, String role, String content) {
    Message message = Message.builder()
        .conversation(conversation)
        .role(role)
        .content(content)
        .timestamp(LocalDateTime.now())
        .tokenCount(estimateTokens(content))
        .metadata(new HashMap<>())
        .build();
    return messageRepository.save(message);
  }

  /**
   * Generate mock response based on query patterns.
   * In production, this would call the LLM service.
   */
  private String generateMockResponse(String query) {
    String lowerQuery = query.toLowerCase();

    if (lowerQuery.contains("rag") || lowerQuery.contains("retrieval")) {
      return "RAG (Retrieval-Augmented Generation) enhances language models by retrieving relevant "
          + "information from a knowledge base before generating responses. This grounds the model's "
          + "outputs in factual data and significantly reduces hallucinations.";
    } else if (lowerQuery.contains("agent") || lowerQuery.contains("agentic")) {
      return "Agentic AI systems are autonomous agents that can plan, reason, use tools, and execute "
          + "multi-step workflows. Unlike traditional chatbots, agents can decompose complex tasks, "
          + "select appropriate tools, and iterate until goals are achieved.";
    } else if (lowerQuery.contains("how") || lowerQuery.contains("work")) {
      return "The system works by: (1) Planning - decomposing your query into subtasks, "
          + "(2) Retrieval - searching the knowledge base for relevant information, "
          + "(3) Reasoning - analyzing retrieved context, and "
          + "(4) Generation - producing a well-grounded response.";
    } else {
      return "I understand your question: \"" + query + "\". This is a mock response from the "
          + "Agentic-RAG system. In production, I would retrieve relevant documents from the "
          + "knowledge base and generate a comprehensive answer grounded in that context.";
    }
  }

  /**
   * Estimate token count (rough approximation).
   */
  private Integer estimateTokens(String... texts) {
    int totalWords = 0;
    for (String text : texts) {
      if (text != null) {
        totalWords += text.split("\\s+").length;
      }
    }
    return (int) (totalWords * 1.3); // Rough approximation: 1 token ≈ 0.75 words
  }

  /**
   * Build metadata for response.
   * REQ-SVC-004: Include processing metrics in response metadata.
   *
   * @param conversation the conversation object
   * @param processingTimeMs processing time in milliseconds
   * @return metadata map
   */
  private Map<String, Object> buildMetadata(Conversation conversation, long processingTimeMs) {
    Map<String, Object> metadata = new HashMap<>();
    metadata.put("conversationId", conversation.getId().toString());
    metadata.put("messageCount", conversation.getMessages().size());
    metadata.put("createdAt", conversation.getCreatedAt());
    metadata.put("processingTimeMs", processingTimeMs);
    metadata.put("processingTimeSeconds", TimeUtils.millisecondsToSeconds(processingTimeMs));
    return metadata;
  }

  /**
   * Get conversation history.
   * REQ-SVC-005: Retrieve conversation history with validation.
   *
   * @param sessionId the conversation session ID
   * @return list of messages in chronological order
   * @throws IllegalArgumentException if sessionId is invalid
   * @throws ResourceNotFoundException if conversation doesn't exist
   */
  @Transactional(readOnly = true)
  public List<Message> getConversationHistory(String sessionId) {
    // REQ-001: Validate session ID format
    ValidationUtils.validateUuid(sessionId, "sessionId");

    try {
      UUID conversationId = UUID.fromString(sessionId);

      // Check if conversation exists
      if (!conversationRepository.existsById(conversationId)) {
        throw new ResourceNotFoundException("Conversation", sessionId);
      }

      List<Message> messages = messageRepository
          .findByConversationIdOrderByTimestampAsc(conversationId);

      log.debug("Retrieved {} messages for conversation {}", messages.size(), sessionId);
      return messages;

    } catch (IllegalArgumentException e) {
      log.warn("Invalid session ID format: {}", sessionId);
      throw new IllegalArgumentException("Invalid session ID format: " + sessionId, e);
    }
  }

  /**
   * Delete a conversation and all associated messages.
   * REQ-SVC-006: Support conversation deletion with cascade.
   *
   * @param conversationId the conversation ID to delete
   * @throws ResourceNotFoundException if conversation doesn't exist
   */
  @Transactional
  public void deleteConversation(String conversationId) {
    ValidationUtils.validateUuid(conversationId, "conversationId");

    UUID uuid = UUID.fromString(conversationId);
    Conversation conversation = conversationRepository.findById(uuid)
        .orElseThrow(() -> new ResourceNotFoundException("Conversation", conversationId));

    conversationRepository.delete(conversation);
    log.info("Deleted conversation {} with {} messages",
        conversationId, conversation.getMessages().size());
  }
}
