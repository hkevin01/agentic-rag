# Quick Start Guide

Get Agentic-RAG up and running in 5 minutes!

## Prerequisites Check

```bash
# Check Java version (need 17+)
java -version

# Check Docker
docker --version
docker-compose --version

# Check Maven
mvn --version
```

## Step-by-Step Setup

### 1. Clone and Navigate

```bash
git clone <repository-url>
cd agentic-rag
```

### 2. Start Services

```bash
# Start all Docker services
./scripts/start.sh

# Or manually:
docker-compose up -d
```

### 3. Verify Services

```bash
# Check all services are running
docker-compose ps

# Expected output: All services should show "healthy" or "running"
```

### 4. Build Application

```bash
# Full build with tests
mvn clean package

# Or skip tests for faster build
mvn clean package -DskipTests
```

### 5. Run Application

```bash
# Using Maven
mvn spring-boot:run

# Or using JAR
java -jar target/agentic-rag.jar
```

### 6. Test the API

```bash
# Health check
curl http://localhost:8080/actuator/health

# Sample chat request
curl -X POST http://localhost:8080/api/chat \
  -H "Content-Type: application/json" \
  -d '{
    "query": "What is Agentic RAG?",
    "sessionId": "test-001"
  }'
```

## Common Issues & Solutions

### Port Already in Use

```bash
# Find process using port 8080
lsof -i :8080

# Kill the process
kill -9 <PID>
```

### Docker Services Not Starting

```bash
# Clean up and restart
docker-compose down -v
docker-compose up -d
```

### Build Failures

```bash
# Clean Maven cache
rm -rf ~/.m2/repository
mvn clean install -U
```

## Next Steps

1. Explore API documentation at http://localhost:8080/swagger-ui.html
2. Review the [Project Plan](project-plan.md) for feature roadmap
3. Read [Architecture Documentation](architecture.md) for system design
4. Check [CONTRIBUTING.md](../.github/CONTRIBUTING.md) to contribute

## Useful Commands

```bash
# View logs
./scripts/logs.sh [service-name]

# Stop all services
./scripts/stop.sh

# Reset everything (⚠️ deletes data)
./scripts/reset.sh

# Run tests
mvn test

# Code quality check
mvn checkstyle:check spotbugs:check pmd:check
```

## Development Workflow

1. **Start services**: `./scripts/start.sh`
2. **Make changes**: Edit code in `src/`
3. **Run tests**: `mvn test`
4. **Test locally**: `mvn spring-boot:run`
5. **Commit**: Follow conventional commits format
6. **Push & PR**: Create pull request to `develop` branch

## Resources

- **Documentation**: `/docs` directory
- **API Docs**: http://localhost:8080/swagger-ui.html
- **Health Check**: http://localhost:8080/actuator/health
- **Metrics**: http://localhost:8080/actuator/metrics
- **LLM Mock**: http://localhost:8081
- **Cloud Mock**: http://localhost:8082
- **OpenSearch**: http://localhost:9200
- **OpenSearch Dashboards**: http://localhost:5601 (with dev profile)

Happy coding! 🚀
