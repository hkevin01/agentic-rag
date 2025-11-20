#!/bin/bash

# Colors for output
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${YELLOW}Testing Agentic-RAG Chat API${NC}"
echo "=========================================="

# Test health endpoint
echo -e "\n${YELLOW}1. Testing health endpoint...${NC}"
HEALTH_RESPONSE=$(curl -s http://localhost:8090/api/health)
if [ $? -eq 0 ]; then
  echo -e "${GREEN}✓ Health check passed${NC}"
  echo "Response: $HEALTH_RESPONSE"
else
  echo -e "${RED}✗ Health check failed${NC}"
  exit 1
fi

# Test chat endpoint
echo -e "\n${YELLOW}2. Testing chat endpoint...${NC}"

# Generate a random session ID
SESSION_ID=$(uuidgen 2>/dev/null || cat /proc/sys/kernel/random/uuid)

CHAT_REQUEST='{
  "query": "What is RAG and how does it work?",
  "sessionId": "'"$SESSION_ID"'",
  "userId": "test-user",
  "temperature": 0.7,
  "maxTokens": 500
}'

echo "Request body:"
echo "$CHAT_REQUEST" | jq '.'

CHAT_RESPONSE=$(curl -s -X POST http://localhost:8090/api/chat \
  -H "Content-Type: application/json" \
  -d "$CHAT_REQUEST")

if [ $? -eq 0 ]; then
  echo -e "\n${GREEN}✓ Chat request successful${NC}"
  echo "Response:"
  echo "$CHAT_RESPONSE" | jq '.'

  # Extract and display key fields
  ANSWER=$(echo "$CHAT_RESPONSE" | jq -r '.answer')
  TOKENS=$(echo "$CHAT_RESPONSE" | jq -r '.tokensUsed')
  echo -e "\n${GREEN}Answer Summary:${NC}"
  echo "Tokens used: $TOKENS"
  echo "Answer: $ANSWER"
else
  echo -e "${RED}✗ Chat request failed${NC}"
  exit 1
fi

# Test follow-up question
echo -e "\n${YELLOW}3. Testing follow-up question (same session)...${NC}"

FOLLOWUP_REQUEST='{
  "query": "Can you explain more about the retrieval step?",
  "sessionId": "'"$SESSION_ID"'",
  "userId": "test-user",
  "temperature": 0.7
}'

echo "Request body:"
echo "$FOLLOWUP_REQUEST" | jq '.'

FOLLOWUP_RESPONSE=$(curl -s -X POST http://localhost:8090/api/chat \
  -H "Content-Type: application/json" \
  -d "$FOLLOWUP_REQUEST")

if [ $? -eq 0 ]; then
  echo -e "\n${GREEN}✓ Follow-up request successful${NC}"
  echo "Response:"
  echo "$FOLLOWUP_RESPONSE" | jq '.'
else
  echo -e "${RED}✗ Follow-up request failed${NC}"
  exit 1
fi

echo -e "\n${GREEN}=========================================="
echo -e "All tests passed! ✓${NC}"
