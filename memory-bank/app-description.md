# Agentic-RAG System

## Overview

Agentic-RAG is an enterprise-grade Retrieval-Augmented Generation (RAG) system enhanced with autonomous agent capabilities. Unlike traditional RAG systems that simply retrieve and generate, Agentic-RAG employs intelligent agents that can plan, reason, use tools, and orchestrate complex multi-step workflows to answer user queries.

## Core Purpose

The system bridges the gap between static document retrieval and dynamic intelligent assistance by combining:
- **Retrieval-Augmented Generation (RAG)**: Grounding LLM responses in actual organizational knowledge
- **Agentic AI**: Autonomous planning, tool usage, and multi-step reasoning
- **Enterprise Integration**: Seamless connection to cloud services, databases, and internal systems

## Key Differentiators

### Traditional RAG vs Agentic-RAG

| Feature | Traditional RAG | Agentic-RAG |
|---------|----------------|-------------|
| **Query Processing** | Direct retrieval → generation | Planning → retrieval → reasoning → generation |
| **Tool Usage** | None | Dynamic tool selection and chaining |
| **Multi-step Reasoning** | Single-shot | Iterative with replanning |
| **Context Awareness** | Limited to retrieved docs | Full conversational + episodic memory |
| **Error Handling** | Fail or hallucinate | Retry with alternative strategies |

## Core Features

### 1. Intelligent Document Processing
- Multi-format ingestion (PDF, DOCX, TXT, MD, HTML)
- Semantic chunking with context preservation
- Metadata extraction and enrichment
- Automatic document versioning and updates

### 2. Hybrid Search & Retrieval
- **Dense Vector Search**: Semantic similarity using embeddings
- **Sparse Retrieval**: BM25 keyword matching
- **Reranking**: Cross-encoder models for relevance refinement
- **Contextual Compression**: Extract only relevant snippets

### 3. Agent Framework
- **Planning Agent**: Decomposes complex queries into subtasks
- **Search Agent**: Retrieves relevant documents from knowledge base
- **Cloud Engine Agent**: Interacts with external cloud services (AWS/Azure)
- **Calculator Agent**: Performs mathematical computations
- **Web Search Agent**: Fetches real-time information

### 4. Memory Systems
- **Short-term Memory**: Conversation context within session
- **Long-term Memory**: Persistent conversation history
- **Episodic Memory**: User preferences and interaction patterns
- **Semantic Memory**: Knowledge graphs and concept relationships

### 5. Conversation Management
- Session tracking with unique IDs
- Message history with role management (user/assistant/system/tool)
- Token counting and budget management
- Conversation summarization for long contexts

## Target Users

### Primary Audience
- **Enterprise Developers**: Building internal AI assistants
- **Data Scientists**: Implementing advanced RAG systems
- **System Integrators**: Connecting AI with existing infrastructure
- **DevOps Engineers**: Deploying and maintaining AI services

### Use Cases
1. **Technical Documentation Assistant**: Navigate complex technical docs with intelligent search
2. **Customer Support Automation**: Retrieve policies and procedures to assist agents
3. **Research Assistant**: Synthesize information from large document repositories
4. **Compliance & Audit**: Query regulations and internal policies
5. **Knowledge Management**: Organizational knowledge discovery and sharing

## Technical Stack

### Backend
- **Language**: Java 17+ (LTS)
- **Framework**: Spring Boot 3.x
- **Build Tool**: Maven 3.8+
- **Database**: PostgreSQL 16 with pgvector extension
- **Search Engine**: OpenSearch 2.15
- **Caching**: Redis 7
- **Testing**: JUnit 5, Mockito, Testcontainers

### Mock Services (Local Development)
- **LLM Mock**: Spring Boot service emulating Azure OpenAI API
- **Cloud Mock**: Spring Boot service emulating AWS/Azure cloud services
- **Containerization**: Docker Compose for local orchestration

### Data Flow
```
User Query
    ↓
Planning Agent (determines strategy)
    ↓
Tool Selection (search, compute, API calls)
    ↓
Information Retrieval (vector search + BM25)
    ↓
Context Assembly (relevant docs + chat history)
    ↓
LLM Generation (with grounded context)
    ↓
Response Validation & Refinement
    ↓
Final Answer to User
```

## Project Goals

### Short-term (Phase 1-2)
- ✅ Establish project structure and Docker environment
- ✅ Implement core RAG pipeline (ingest, embed, retrieve)
- ✅ Create basic agent framework with tool registry
- ✅ Build REST API for chat and document ingestion

### Medium-term (Phase 3-5)
- ⏳ Advanced agent capabilities (replanning, multi-agent collaboration)
- ⏳ Comprehensive testing (unit, integration, performance)
- ⏳ Monitoring and observability (metrics, tracing, logging)
- ⏳ Security hardening (authentication, authorization, rate limiting)

### Long-term (Phase 6-7)
- 🎯 Production deployment guide (Kubernetes, cloud providers)
- 🎯 Advanced RAG techniques (HYDE, query rewriting, parent-doc retrieval)
- 🎯 Multi-modal support (images, audio, video)
- 🎯 Fine-tuning and model customization
- 🎯 Plugin system for custom tools and agents

## Intended Audience

This project is designed for organizations that need:
- **Enterprise-grade reliability**: Robust error handling, monitoring, graceful degradation
- **Security compliance**: Authentication, authorization, audit logging
- **Scalability**: Horizontal scaling, caching, async processing
- **Extensibility**: Plugin architecture for custom tools and integrations
- **Maintainability**: Clean code, comprehensive tests, documentation

## Why Local Development with Mocks?

Many enterprises face constraints accessing cloud services during development:
- **Security policies**: Restricted API access
- **Cost concerns**: Avoid unnecessary cloud charges
- **Network limitations**: Air-gapped or restricted networks
- **Testing requirements**: Need deterministic responses for automated tests

Our approach:
1. **All dependencies dockerized**: PostgreSQL, OpenSearch, Redis, LLM mock, Cloud mock
2. **Drop-in replacement**: Change configuration to switch from mock to real services
3. **Contract testing**: Mocks implement same API contracts as real services
4. **Development velocity**: No waiting for cloud provisioning or approvals

## Project Status

**Current Phase**: Foundation & Infrastructure Setup (Phase 1)

See `docs/project-plan.md` for detailed roadmap and task tracking.

---

**Last Updated**: 2025-11-19
**Maintainers**: Agentic-RAG Development Team
**License**: MIT (or as per organization policy)
