-- Enable pgvector extension for vector embeddings
CREATE EXTENSION IF NOT EXISTS vector;

-- Create schema for RAG system
CREATE SCHEMA IF NOT EXISTS rag;

-- Set default schema
SET search_path TO rag, public;

-- Conversations table for tracking chat sessions
CREATE TABLE IF NOT EXISTS conversations (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id VARCHAR(255),
  title VARCHAR(500),
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  metadata JSONB,
  status VARCHAR(50) DEFAULT 'active',
  CONSTRAINT valid_status CHECK (status IN ('active', 'archived', 'deleted'))
);

-- Messages table for individual chat messages
CREATE TABLE IF NOT EXISTS messages (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  conversation_id UUID NOT NULL REFERENCES conversations(id) ON DELETE CASCADE,
  role VARCHAR(50) NOT NULL,
  content TEXT NOT NULL,
  timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  token_count INTEGER,
  metadata JSONB,
  CONSTRAINT valid_role CHECK (role IN ('user', 'assistant', 'system', 'tool'))
);

-- Documents table for ingested documents
CREATE TABLE IF NOT EXISTS documents (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  title VARCHAR(1000),
  source VARCHAR(500),
  content TEXT NOT NULL,
  content_hash VARCHAR(64) UNIQUE,
  document_type VARCHAR(100),
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  metadata JSONB,
  status VARCHAR(50) DEFAULT 'indexed',
  CONSTRAINT valid_doc_status CHECK (status IN ('pending', 'indexed', 'failed', 'deleted'))
);

-- Document chunks for RAG retrieval
CREATE TABLE IF NOT EXISTS document_chunks (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  document_id UUID NOT NULL REFERENCES documents(id) ON DELETE CASCADE,
  chunk_index INTEGER NOT NULL,
  content TEXT NOT NULL,
  token_count INTEGER,
  start_char INTEGER,
  end_char INTEGER,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  metadata JSONB,
  UNIQUE (document_id, chunk_index)
);

-- Embeddings table for vector search (using pgvector)
CREATE TABLE IF NOT EXISTS embeddings (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  chunk_id UUID NOT NULL REFERENCES document_chunks(id) ON DELETE CASCADE,
  embedding vector(1536),
  model_name VARCHAR(100) NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  UNIQUE (chunk_id, model_name)
);

-- Agent executions for tracking agent workflows
CREATE TABLE IF NOT EXISTS agent_executions (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  conversation_id UUID REFERENCES conversations(id) ON DELETE SET NULL,
  agent_type VARCHAR(100) NOT NULL,
  input_data JSONB NOT NULL,
  output_data JSONB,
  status VARCHAR(50) NOT NULL DEFAULT 'pending',
  started_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  completed_at TIMESTAMP,
  error_message TEXT,
  execution_time_ms INTEGER,
  CONSTRAINT valid_exec_status CHECK (status IN ('pending', 'running', 'completed', 'failed', 'cancelled'))
);

-- Tool invocations for tracking tool usage
CREATE TABLE IF NOT EXISTS tool_invocations (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  execution_id UUID NOT NULL REFERENCES agent_executions(id) ON DELETE CASCADE,
  tool_name VARCHAR(100) NOT NULL,
  input_params JSONB NOT NULL,
  output_result JSONB,
  status VARCHAR(50) NOT NULL DEFAULT 'pending',
  started_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  completed_at TIMESTAMP,
  error_message TEXT,
  execution_time_ms INTEGER,
  CONSTRAINT valid_tool_status CHECK (status IN ('pending', 'running', 'completed', 'failed'))
);

-- Create indexes for performance
CREATE INDEX idx_conversations_user_id ON conversations(user_id);
CREATE INDEX idx_conversations_created_at ON conversations(created_at DESC);
CREATE INDEX idx_messages_conversation_id ON messages(conversation_id);
CREATE INDEX idx_messages_timestamp ON messages(timestamp DESC);
CREATE INDEX idx_documents_content_hash ON documents(content_hash);
CREATE INDEX idx_documents_created_at ON documents(created_at DESC);
CREATE INDEX idx_document_chunks_document_id ON document_chunks(document_id);
CREATE INDEX idx_embeddings_chunk_id ON embeddings(chunk_id);
CREATE INDEX idx_agent_executions_conversation_id ON agent_executions(conversation_id);
CREATE INDEX idx_agent_executions_started_at ON agent_executions(started_at DESC);
CREATE INDEX idx_tool_invocations_execution_id ON tool_invocations(execution_id);

-- Vector similarity search indexes
CREATE INDEX idx_embeddings_vector_cosine ON embeddings USING hnsw (embedding vector_cosine_ops);
CREATE INDEX idx_embeddings_vector_l2 ON embeddings USING hnsw (embedding vector_l2_ops);

-- Function to update updated_at timestamp
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
  NEW.updated_at = CURRENT_TIMESTAMP;
  RETURN NEW;
END;
$$ language 'plpgsql';

-- Triggers for automatic timestamp updates
CREATE TRIGGER update_conversations_updated_at BEFORE UPDATE ON conversations
  FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_documents_updated_at BEFORE UPDATE ON documents
  FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- Grant permissions
GRANT ALL PRIVILEGES ON SCHEMA rag TO rag_user;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA rag TO rag_user;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA rag TO rag_user;
