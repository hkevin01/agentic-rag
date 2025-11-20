#!/bin/bash
# View logs from all services

SERVICE=${1:-all}

if [ "$SERVICE" = "all" ]; then
    docker-compose logs -f
else
    docker-compose logs -f "$SERVICE"
fi
