#!/bin/bash

# Variables
CONTAINER_NAME=test-pg

# Check if postgres container is actually running
RUNNING=$(docker inspect --format="{{ .State.Running }}" $CONTAINER_NAME 2> /dev/null)

if [ "$RUNNING" != "true" ]; then
    echo "Postgres is already stopped"
else
    echo "Stopping Postgres..."
    # Exit the container
    docker stop $CONTAINER_NAME
    echo "Postgres has stopped"
fi

docker rm $CONTAINER_NAME
