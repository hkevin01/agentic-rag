#!/bin/bash
# Start all services and the application

set -e

echo "🚀 Starting Agentic-RAG stack..."

# Start Docker services
echo "📦 Starting Docker services..."
docker-compose up -d

# Wait for services to be healthy
echo "⏳ Waiting for services to be ready..."
sleep 10

# Check service health
echo "🔍 Checking service health..."
docker-compose ps

# Build the application if not already built
if [ ! -f "target/agentic-rag.jar" ]; then
    echo "🔨 Building application..."
    mvn clean package -DskipTests
fi

# Start the application
echo "▶️  Starting Agentic-RAG application..."
mvn spring-boot:run

