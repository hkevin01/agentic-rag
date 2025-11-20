#!/bin/bash
# Stop all services

set -e

echo "🛑 Stopping Agentic-RAG stack..."

# Stop Docker services
docker-compose down

echo "✅ All services stopped"
