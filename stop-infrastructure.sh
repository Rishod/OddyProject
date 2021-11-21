#!/bin/bash

# Check if postgres container is actually running
KAFKA_CONTAINER_NAME=oddy-kafka
ZOOKEEPER_CONTAINER_NAME=oddy-zookeeper
DOCKER_NETWORK=kafka-network

### Zookeeper
RUNNING=$(docker inspect --format="{{ .State.Running }}" $ZOOKEEPER_CONTAINER_NAME 2> /dev/null)

if [ "$RUNNING" != "true" ]; then
    echo "Zookeeper is already stopped"
else
    echo "Stopping Zookeeper..."
    # Exit the container
    docker stop $ZOOKEEPER_CONTAINER_NAME
    echo "Zookeeper has stopped"
fi

# Remove container
docker rm $ZOOKEEPER_CONTAINER_NAME

### Kafka
RUNNING=$(docker inspect --format="{{ .State.Running }}" $KAFKA_CONTAINER_NAME 2> /dev/null)

if [ "$RUNNING" != "true" ]; then
    echo "Kafka is already stopped"
else
    echo "Stopping Kafka..."
    # Exit the container
    docker stop $KAFKA_CONTAINER_NAME
    echo "Kafka has stopped"
fi

# Remove container
docker rm $KAFKA_CONTAINER_NAME

### Remove docker network
docker network rm $DOCKER_NETWORK || true

