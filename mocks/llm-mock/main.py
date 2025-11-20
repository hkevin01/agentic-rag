"""
LLM Mock Service - Emulates Azure OpenAI / OpenAI API
Provides deterministic responses for local development and testing
"""
from fastapi import FastAPI, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel, Field
from typing import List, Optional, Dict, Any
import uvicorn
import uuid
import time
from datetime import datetime

app = FastAPI(
    title="LLM Mock Service",
    description="Mock LLM API compatible with Azure OpenAI",
    version="1.0.0"
)

# CORS middleware
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# Request/Response Models
class Message(BaseModel):
    role: str = Field(..., description="Role: user, assistant, system, or tool")
    content: str = Field(..., description="Message content")
    name: Optional[str] = Field(None, description="Name of the tool/function")

class ChatCompletionRequest(BaseModel):
    model: str = Field(default="gpt-4", description="Model name")
    messages: List[Message] = Field(..., description="Conversation messages")
    temperature: Optional[float] = Field(0.7, ge=0.0, le=2.0)
    max_tokens: Optional[int] = Field(1000, ge=1)
    top_p: Optional[float] = Field(1.0, ge=0.0, le=1.0)
    stream: Optional[bool] = Field(False)
    tools: Optional[List[Dict[str, Any]]] = None

class Choice(BaseModel):
    index: int
    message: Message
    finish_reason: str

class Usage(BaseModel):
    prompt_tokens: int
    completion_tokens: int
    total_tokens: int

class ChatCompletionResponse(BaseModel):
    id: str
    object: str = "chat.completion"
    created: int
    model: str
    choices: List[Choice]
    usage: Usage

class EmbeddingRequest(BaseModel):
    input: str | List[str]
    model: str = Field(default="text-embedding-ada-002")

class EmbeddingData(BaseModel):
    object: str = "embedding"
    embedding: List[float]
    index: int

class EmbeddingResponse(BaseModel):
    object: str = "list"
    data: List[EmbeddingData]
    model: str
    usage: Usage

# Mock responses based on query patterns
def generate_mock_response(messages: List[Message], model: str) -> str:
    """Generate contextual mock responses based on query patterns"""
    last_user_message = next(
        (msg.content for msg in reversed(messages) if msg.role == "user"),
        ""
    ).lower()
    
    # Pattern matching for different query types
    if "rag" in last_user_message or "retrieval" in last_user_message:
        return (
            "RAG (Retrieval-Augmented Generation) is a technique that enhances language "
            "models by retrieving relevant information from a knowledge base before "
            "generating responses. This grounds the model's outputs in factual data and "
            "reduces hallucinations."
        )
    elif "agent" in last_user_message or "agentic" in last_user_message:
        return (
            "Agentic AI systems are autonomous agents that can plan, reason, use tools, "
            "and execute multi-step workflows. Unlike traditional chatbots, agents can "
            "decompose complex tasks, select appropriate tools, and iterate until goals "
            "are achieved."
        )
    elif "search" in last_user_message or "find" in last_user_message:
        return (
            "I'll search the knowledge base for relevant information. Based on the query, "
            "I recommend using hybrid search combining vector similarity and keyword "
            "matching for optimal results."
        )
    elif "error" in last_user_message or "problem" in last_user_message:
        return (
            "I detect a potential issue. Let me investigate:\n"
            "1. Check configuration settings\n"
            "2. Verify service connectivity\n"
            "3. Review error logs\n"
            "Please provide more details about the specific error you're encountering."
        )
    elif "?" in last_user_message:
        return (
            f"[MOCK-{model}] Thank you for your question. Based on the available "
            "information, here's what I can tell you: This is a mock response demonstrating "
            "the LLM API functionality. In production, this would be replaced with actual "
            "AI-generated content."
        )
    else:
        return (
            f"[MOCK-{model}] I understand your request. This is a simulated response "
            "from the LLM mock service. In a production environment, this would be "
            "replaced with actual AI-generated content based on your query and retrieved "
            "context from the knowledge base."
        )

# API Endpoints
@app.get("/")
async def root():
    """Health check endpoint"""
    return {
        "service": "LLM Mock Service",
        "status": "healthy",
        "version": "1.0.0",
        "timestamp": datetime.utcnow().isoformat()
    }

@app.get("/health")
async def health():
    """Detailed health check"""
    return {
        "status": "healthy",
        "uptime_seconds": time.process_time(),
        "timestamp": datetime.utcnow().isoformat()
    }

@app.post("/v1/chat/completions", response_model=ChatCompletionResponse)
async def chat_completions(request: ChatCompletionRequest):
    """
    Mock chat completions endpoint compatible with OpenAI API
    """
    try:
        # Generate mock response
        response_content = generate_mock_response(request.messages, request.model)
        
        # Calculate mock token counts (rough estimate)
        prompt_tokens = sum(len(msg.content.split()) for msg in request.messages)
        completion_tokens = len(response_content.split())
        
        # Simulate processing delay
        time.sleep(0.1)
        
        return ChatCompletionResponse(
            id=f"chatcmpl-{uuid.uuid4().hex[:24]}",
            object="chat.completion",
            created=int(time.time()),
            model=request.model,
            choices=[
                Choice(
                    index=0,
                    message=Message(role="assistant", content=response_content),
                    finish_reason="stop"
                )
            ],
            usage=Usage(
                prompt_tokens=prompt_tokens,
                completion_tokens=completion_tokens,
                total_tokens=prompt_tokens + completion_tokens
            )
        )
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

@app.post("/v1/embeddings", response_model=EmbeddingResponse)
async def create_embeddings(request: EmbeddingRequest):
    """
    Mock embeddings endpoint - returns deterministic fake embeddings
    """
    inputs = [request.input] if isinstance(request.input, str) else request.input
    
    # Generate fake but consistent 1536-dimensional embeddings
    embeddings_data = []
    for idx, text in enumerate(inputs):
        # Deterministic "embedding" based on text hash
        hash_val = hash(text)
        fake_embedding = [
            (hash_val + i * 13) % 1000 / 1000.0 - 0.5
            for i in range(1536)
        ]
        
        embeddings_data.append(
            EmbeddingData(
                object="embedding",
                embedding=fake_embedding,
                index=idx
            )
        )
    
    total_tokens = sum(len(text.split()) for text in inputs)
    
    return EmbeddingResponse(
        object="list",
        data=embeddings_data,
        model=request.model,
        usage=Usage(
            prompt_tokens=total_tokens,
            completion_tokens=0,
            total_tokens=total_tokens
        )
    )

@app.get("/v1/models")
async def list_models():
    """List available mock models"""
    return {
        "object": "list",
        "data": [
            {"id": "gpt-4", "object": "model", "created": 1687882411, "owned_by": "mock"},
            {"id": "gpt-3.5-turbo", "object": "model", "created": 1687882411, "owned_by": "mock"},
            {"id": "text-embedding-ada-002", "object": "model", "created": 1687882411, "owned_by": "mock"},
        ]
    }

if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=8080, log_level="info")
