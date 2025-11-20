package com.enterprise.rag.service;

import com.enterprise.rag.domain.Conversation;
import com.enterprise.rag.domain.Message;
import com.enterprise.rag.dto.ChatRequest;
import com.enterprise.rag.dto.ChatResponse;
import com.enterprise.rag.repository.ConversationRepository;
import com.enterprise.rag.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

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
   * @param request the chat request
   * @return chat response
   */
  @Transactional
  public ChatResponse chat(ChatRequest request) {
    log.info("Processing chat request for session: {}", request.getSessionId());

    try {
      // Get or create conversation
      Conversation conversation = getOrCreateConversation(request.getSessionId(), request.getUserId());

      // Save user message
      Message userMessage = saveMessage(conversation, "user", request.getQuery());

      // Generate response (mocked for now)
      String answer = generateMockResponse(request.getQuery());

      // Save assistant message
      Message assistantMessage = saveMessage(conversation, "assistant", answer);

      // Build response
      return ChatResponse.builder()
          .answer(answer)
          .sessionId(request.getSessionId())
          .conversationId(conversation.getId().toString())
          .sources(Collections.emptyList())
          .metadata(buildMetadata(conversation))
          .timestamp(LocalDateTime.now())
          .tokensUsed(estimateTokens(request.getQuery(), answer))
          .model("gpt-4-mock")
          .build();

    } catch (Exception e) {
      log.error("Error processing chat request: {}", e.getMessage(), e);
      return ChatResponse.builder()
          .answer("I apologize, but I encountered an error processing your request. Please try again.")
          .sessionId(request.getSessionId())
          .timestamp(LocalDateTime.now())
          .model("error")
          .build();
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
   */
  private Map<String, Object> buildMetadata(Conversation conversation) {
    Map<String, Object> metadata = new HashMap<>();
    metadata.put("conversationId", conversation.getId().toString());
    metadata.put("messageCount", conversation.getMessages().size());
    metadata.put("createdAt", conversation.getCreatedAt());
    return metadata;
  }

  /**
   * Get conversation history.
   */
  @Transactional(readOnly = true)
  public List<Message> getConversationHistory(String sessionId) {
    try {
      UUID conversationId = UUID.fromString(sessionId);
      return messageRepository.findByConversationIdOrderByTimestampAsc(conversationId);
    } catch (IllegalArgumentException e) {
      log.warn("Invalid session ID format: {}", sessionId);
      return Collections.emptyList();
    }
  }
}
