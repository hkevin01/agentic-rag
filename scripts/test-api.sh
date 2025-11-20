#!/bin/bash
# Test the API endpoints

set -e

BASE_URL="http://localhost:8080"

echo "🧪 Testing Agentic-RAG API..."

# Health check
echo -e "\n1️⃣  Health Check"
curl -s "$BASE_URL/actuator/health" | jq '.'

# Chat endpoint
echo -e "\n2️⃣  Chat Endpoint"
curl -s -X POST "$BASE_URL/api/chat" \
  -H "Content-Type: application/json" \
  -d '{
    "query": "What is Agentic RAG?",
    "sessionId": "test-001"
  }' | jq '.'

echo -e "\n✅ API tests complete!"
