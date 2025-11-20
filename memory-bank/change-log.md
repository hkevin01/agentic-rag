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
