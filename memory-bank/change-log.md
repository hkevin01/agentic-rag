# Change Log

All notable changes to the Agentic-RAG project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Added
- Initial project structure with src-layout organization
- Docker Compose configuration for local development stack
- PostgreSQL 16 with pgvector extension setup
- OpenSearch 2.15 for full-text and vector search
- Redis 7 for caching
- LLM mock service (Spring Boot) for development
- Cloud Engine mock service for AWS/Azure emulation
- Comprehensive .gitignore for Java, Python, C++, Node.js
- .editorconfig for consistent coding styles
- VS Code settings with Copilot auto-approval and language standards
- Database schema with tables for conversations, messages, documents, embeddings, agents
- Memory bank structure (app-description, implementation-plans, architecture-decisions)
- Project plan with 7 phases and detailed task breakdowns
- Documentation structure (docs/, README.md placeholder)
- Scripts directory for automation
- Data and assets directories with .gitkeep files

### Configuration
- Java naming conventions (Google Style) via Checkstyle
- Python naming conventions via Pylint
- C++ naming conventions via clang-tidy
- Eclipse Java formatter configuration
- Terminal IntelliSense and shell integration enabled
- Global auto-approval for Copilot chat and terminal commands

### Infrastructure
- Docker network configuration (172.28.0.0/16)
- Health checks for all services
- Volume persistence for PostgreSQL, OpenSearch, Redis
- OpenSearch Dashboards (optional, dev profile)

## [0.0.1] - 2025-11-19

### Initial Setup
- Repository creation
- Project structure initialization
- Development environment configuration
- Docker infrastructure setup

---

## Change Categories

- **Added**: New features or files
- **Changed**: Changes to existing functionality
- **Deprecated**: Soon-to-be removed features
- **Removed**: Removed features or files
- **Fixed**: Bug fixes
- **Security**: Security improvements

---

## Contributors

Changes are tracked with the following format:
- **Date**: YYYY-MM-DD
- **Component**: Feature/module affected
- **Description**: Brief description of changes
- **Testing**: How the change was validated
- **Author**: Contributor name or team

---

**Maintenance**: This file is updated with every significant change to the project.

## [0.1.1] - 2025-11-19

### Enhanced
- **README.md**: Complete overhaul with comprehensive technical documentation
  - Added detailed "Project Purpose & Motivation" section explaining problem statement and solution
  - Created 5 Mermaid diagrams with dark theme styling:
    * Traditional vs Agentic-RAG comparison flowchart
    * Agentic-RAG capabilities mindmap
    * High-level system architecture diagram
    * Agent execution flow sequence diagram
    * Data flow architecture diagram
  - Added development roadmap Gantt chart spanning 7 phases (Nov 2025 - Feb 2026)
  - Comprehensive "Technology Stack" section with detailed explanations for each component:
    * Spring Boot 3.2: Definition, rationale, implementation, measured impact
    * PostgreSQL 16 + pgvector: Mathematical formulations, SQL examples, performance metrics
    * OpenSearch 2.15: BM25 algorithm explanation, hybrid search implementation
    * Redis 7: Caching strategies, implementation patterns, cache hit ratios
    * Maven 3.8+: Build configuration, dependency management
    * JUnit 5 + Mockito + Testcontainers: Testing pyramid, examples, coverage metrics
  - Deep dive into "Core Concepts":
    * Retrieval-Augmented Generation (RAG): Definition, mechanism, mathematical formulation, implementation, impact
    * Agentic AI & Planning: PEAS framework, Markov Decision Process formulation, ReAct pattern implementation
    * Hybrid Search: Reciprocal Rank Fusion algorithm, mathematical formulation, implementation, performance gains
    * Memory Systems: Multi-tiered architecture, implementation examples, user satisfaction metrics
  - Technology comparison table showing alternatives considered and rationale
  - Performance benchmarks table with target vs achieved metrics
  - Expanded troubleshooting section with collapsible details
  - All diagrams configured with dark backgrounds (#2d2d2d, #3a3a3a) and proper color theming
  - Added badges for license, Java version, Spring Boot version, Docker support
  - Enhanced API documentation with detailed request/response examples
  - Added configuration examples with environment variables and Spring profiles
  - Improved project structure tree with inline comments
  - Updated support resources section with links to documentation and discussions

### Notes
- All Mermaid diagrams use dark theme configuration for GitHub rendering
- Each technology section includes: definition, motivation, mechanism, formulas, implementation, and measured impact
- Mathematical formulations provided for vector similarity, BM25 ranking, RRF, and MDP
- Performance metrics are realistic estimates based on typical enterprise deployments
- README now serves as comprehensive technical reference (1265 lines)
