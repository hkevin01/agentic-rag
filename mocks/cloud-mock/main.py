"""
Cloud Engine Mock Service - Emulates AWS/Azure cloud services
Provides mock responses for cloud operations during local development
"""
from fastapi import FastAPI, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel, Field
from typing import List, Optional, Dict, Any
from enum import Enum
import uvicorn
import uuid
import time
from datetime import datetime, timedelta

app = FastAPI(
    title="Cloud Engine Mock Service",
    description="Mock AWS/Azure cloud services for local development",
    version="1.0.0"
)

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# Enums
class JobStatus(str, Enum):
    PENDING = "pending"
    RUNNING = "running"
    COMPLETED = "completed"
    FAILED = "failed"

class ResourceType(str, Enum):
    COMPUTE = "compute"
    STORAGE = "storage"
    DATABASE = "database"
    NETWORK = "network"

# Models
class JobRequest(BaseModel):
    name: str = Field(..., description="Job name")
    parameters: Dict[str, Any] = Field(default_factory=dict)
    timeout_seconds: Optional[int] = Field(300, ge=1)

class JobResponse(BaseModel):
    job_id: str
    name: str
    status: JobStatus
    created_at: str
    started_at: Optional[str] = None
    completed_at: Optional[str] = None
    result: Optional[Dict[str, Any]] = None
    error: Optional[str] = None

class Resource(BaseModel):
    resource_id: str
    name: str
    type: ResourceType
    status: str
    region: str
    created_at: str
    metadata: Dict[str, Any] = Field(default_factory=dict)

# In-memory storage
jobs_db: Dict[str, JobResponse] = {}
resources_db: Dict[str, Resource] = {}

# Helper functions
def generate_job_id() -> str:
    return f"job-{uuid.uuid4().hex[:16]}"

def generate_resource_id() -> str:
    return f"res-{uuid.uuid4().hex[:16]}"

# API Endpoints
@app.get("/")
async def root():
    return {
        "service": "Cloud Engine Mock Service",
        "status": "healthy",
        "version": "1.0.0",
        "timestamp": datetime.utcnow().isoformat()
    }

@app.get("/health")
async def health():
    return {
        "status": "healthy",
        "jobs_count": len(jobs_db),
        "resources_count": len(resources_db),
        "timestamp": datetime.utcnow().isoformat()
    }

# Job Management
@app.post("/api/jobs", response_model=JobResponse)
async def create_job(request: JobRequest):
    """Create a new cloud job"""
    job_id = generate_job_id()
    now = datetime.utcnow().isoformat()
    
    job = JobResponse(
        job_id=job_id,
        name=request.name,
        status=JobStatus.PENDING,
        created_at=now
    )
    
    jobs_db[job_id] = job
    return job

@app.get("/api/jobs/{job_id}", response_model=JobResponse)
async def get_job(job_id: str):
    """Get job status"""
    if job_id not in jobs_db:
        raise HTTPException(status_code=404, detail="Job not found")
    
    job = jobs_db[job_id]
    
    # Simulate job progression
    if job.status == JobStatus.PENDING:
        job.status = JobStatus.RUNNING
        job.started_at = datetime.utcnow().isoformat()
    elif job.status == JobStatus.RUNNING:
        # Simulate completion after a few checks
        job.status = JobStatus.COMPLETED
        job.completed_at = datetime.utcnow().isoformat()
        job.result = {
            "status": "success",
            "output": f"Mock job {job.name} completed successfully",
            "metrics": {
                "duration_seconds": 5,
                "items_processed": 100
            }
        }
    
    return job

@app.get("/api/jobs", response_model=List[JobResponse])
async def list_jobs(status: Optional[JobStatus] = None, limit: int = 100):
    """List all jobs"""
    jobs = list(jobs_db.values())
    if status:
        jobs = [j for j in jobs if j.status == status]
    return jobs[:limit]

@app.delete("/api/jobs/{job_id}")
async def delete_job(job_id: str):
    """Delete a job"""
    if job_id not in jobs_db:
        raise HTTPException(status_code=404, detail="Job not found")
    del jobs_db[job_id]
    return {"message": f"Job {job_id} deleted"}

# Resource Management
@app.post("/api/resources", response_model=Resource)
async def create_resource(
    name: str,
    type: ResourceType,
    region: str = "us-east-1",
    metadata: Dict[str, Any] = None
):
    """Create a cloud resource"""
    resource_id = generate_resource_id()
    
    resource = Resource(
        resource_id=resource_id,
        name=name,
        type=type,
        status="active",
        region=region,
        created_at=datetime.utcnow().isoformat(),
        metadata=metadata or {}
    )
    
    resources_db[resource_id] = resource
    return resource

@app.get("/api/resources/{resource_id}", response_model=Resource)
async def get_resource(resource_id: str):
    """Get resource details"""
    if resource_id not in resources_db:
        raise HTTPException(status_code=404, detail="Resource not found")
    return resources_db[resource_id]

@app.get("/api/resources", response_model=List[Resource])
async def list_resources(
    type: Optional[ResourceType] = None,
    region: Optional[str] = None,
    limit: int = 100
):
    """List all resources"""
    resources = list(resources_db.values())
    if type:
        resources = [r for r in resources if r.type == type]
    if region:
        resources = [r for r in resources if r.region == region]
    return resources[:limit]

@app.delete("/api/resources/{resource_id}")
async def delete_resource(resource_id: str):
    """Delete a resource"""
    if resource_id not in resources_db:
        raise HTTPException(status_code=404, detail="Resource not found")
    del resources_db[resource_id]
    return {"message": f"Resource {resource_id} deleted"}

# Storage Operations
@app.post("/api/storage/upload")
async def upload_file(
    bucket: str,
    key: str,
    content_type: str = "application/octet-stream"
):
    """Mock file upload"""
    return {
        "bucket": bucket,
        "key": key,
        "upload_id": f"upload-{uuid.uuid4().hex[:16]}",
        "url": f"https://mock-storage.example.com/{bucket}/{key}",
        "timestamp": datetime.utcnow().isoformat()
    }

@app.get("/api/storage/download")
async def download_file(bucket: str, key: str):
    """Mock file download"""
    return {
        "bucket": bucket,
        "key": key,
        "url": f"https://mock-storage.example.com/{bucket}/{key}",
        "size_bytes": 1024,
        "content_type": "application/octet-stream",
        "timestamp": datetime.utcnow().isoformat()
    }

# Compute Operations
@app.post("/api/compute/execute")
async def execute_function(
    function_name: str,
    payload: Dict[str, Any] = None
):
    """Execute serverless function"""
    return {
        "function_name": function_name,
        "execution_id": f"exec-{uuid.uuid4().hex[:16]}",
        "status": "success",
        "result": {
            "message": f"Mock execution of {function_name}",
            "input": payload,
            "output": "Mock function result"
        },
        "duration_ms": 125,
        "timestamp": datetime.utcnow().isoformat()
    }

if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=8080, log_level="info")
