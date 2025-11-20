# Data Directory

This directory is for storing local data files used by the application.

## Structure

- `embeddings/` - Cached embedding vectors
- `documents/` - Uploaded documents for ingestion
- `exports/` - Exported data and reports
- `temp/` - Temporary processing files

## Usage

Place data files here during development. In production, use external storage (S3, Azure Blob, etc.).

**Note**: This directory is gitignored except for this README and .gitkeep files.
