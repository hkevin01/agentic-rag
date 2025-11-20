#!/bin/bash
# Reset all data and restart services

set -e

echo "⚠️  Resetting Agentic-RAG stack..."
read -p "This will delete all data. Continue? (y/N) " -n 1 -r
echo
if [[ ! $REPLY =~ ^[Yy]$ ]]; then
    echo "Cancelled"
    exit 1
fi

# Stop and remove volumes
echo "🗑️  Removing containers and volumes..."
docker-compose down -v

# Remove build artifacts
echo "🧹 Cleaning build artifacts..."
mvn clean

# Restart services
echo "🔄 Restarting services..."
docker-compose up -d

echo "✅ Reset complete! Services are starting..."
