# Contributing to Agentic-RAG

Thank you for your interest in contributing! This document provides guidelines for contributing to the project.

## Code of Conduct

- Be respectful and inclusive
- Focus on constructive feedback
- Help maintain a positive community

## How to Contribute

### 1. Fork and Clone

```bash
git clone https://github.com/YOUR_USERNAME/agentic-rag.git
cd agentic-rag
```

### 2. Create a Branch

```bash
git checkout -b feature/your-feature-name
```

Use branch naming conventions:
- `feature/` - New features
- `bugfix/` - Bug fixes
- `docs/` - Documentation updates
- `refactor/` - Code refactoring
- `test/` - Test additions/improvements

### 3. Make Changes

- Follow code style guidelines (see below)
- Write clear commit messages
- Add tests for new features
- Update documentation as needed

### 4. Test Your Changes

```bash
# Run unit tests
mvn test

# Run integration tests
mvn verify

# Start local Docker stack
docker-compose up -d

# Test manually
curl -X POST http://localhost:8080/api/chat \
  -H "Content-Type: application/json" \
  -d '{"query": "Test query", "sessionId": "test"}'
```

### 5. Submit a Pull Request

- Push your changes to your fork
- Create a pull request to the `develop` branch
- Fill out the PR template completely
- Wait for code review

## Code Style Guidelines

### Java
- **Style**: Google Java Style Guide
- **Indentation**: 2 spaces
- **Line length**: 100 characters
- **Naming**:
  - Classes: `PascalCase`
  - Methods/Variables: `camelCase`
  - Constants: `UPPER_SNAKE_CASE`
  - Packages: `lowercase`

### Python
- **Style**: PEP 8
- **Indentation**: 4 spaces
- **Line length**: 79 characters (docstrings), 100 (code)
- **Naming**:
  - Classes: `PascalCase`
  - Functions/Variables: `snake_case`
  - Constants: `UPPER_SNAKE_CASE`

### C/C++
- **Style**: Google C++ Style Guide
- **Indentation**: 2 spaces
- **Line length**: 100 characters
- **Naming**:
  - Classes: `PascalCase`
  - Functions: `camelBack`
  - Variables: `lower_case`
  - Constants: `UPPER_CASE`

## Commit Message Guidelines

Follow conventional commits format:

```
type(scope): description

[optional body]

[optional footer]
```

**Types:**
- `feat`: New feature
- `fix`: Bug fix
- `docs`: Documentation changes
- `style`: Code style changes (formatting, no logic change)
- `refactor`: Code refactoring
- `test`: Adding or updating tests
- `chore`: Build process or tooling changes

**Examples:**
```
feat(agent): add multi-agent collaboration support

Implements agent delegation and handoff capabilities for
complex multi-step workflows.

Closes #123
```

## Testing Guidelines

### Unit Tests
- Test individual components in isolation
- Use mocks for dependencies
- Aim for >80% code coverage
- Name tests descriptively: `testMethodName_Scenario_ExpectedBehavior`

### Integration Tests
- Test component interactions
- Use Testcontainers for databases
- Test real API endpoints with MockMvc
- Verify end-to-end workflows

### Performance Tests
- Use JMH for micro-benchmarks
- Test critical paths for latency
- Monitor memory usage
- Document performance requirements

## Documentation

- Update README.md for user-facing changes
- Add JavaDoc for public APIs
- Update architecture docs in memory-bank/
- Include code examples for new features
- Keep project-plan.md task list current

## Questions?

- Open an issue for discussion
- Check existing issues and PRs
- Review project documentation in docs/

Thank you for contributing! 🎉
