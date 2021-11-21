#!/bin/bash

### Variables
KAFKA_CONTAINER_NAME=oddy-kafka
KAFKA_IMAGE=confluentinc/cp-kafka:latest

ZOOKEEPER_CONTAINER_NAME=oddy-zookeeper
ZOOKEEPER_IMAGE=confluentinc/cp-zookeeper:latest

DOCKER_NETWORK=kafka-network

### If docker containers have already started, just go ahead and keep running.
echo "Running start-infrastructure.sh from $PWD"

### Docker network
docker network create $DOCKER_NETWORK --driver bridge || true

### Zookeeper
RUNNING=$(docker inspect --format="{{ .State.Running }}" $ZOOKEEPER_CONTAINER_NAME 2> /dev/null)
if [ "$RUNNING" == "true" ];
  then
      echo "Zookeeper is already up and running"
  else
    echo "Running zookeeper..."

    docker run --name $ZOOKEEPER_CONTAINER_NAME \
     --network $DOCKER_NETWORK \
     -e ZOOKEEPER_CLIENT_PORT=2181 \
     -e ZOOKEEPER_TICK_TIME=2000 \
     -d -p 2181:2181 \
     $ZOOKEEPER_IMAGE
fi

echo "Wait for zookeeper to start with retry for 40 seconds"

attempt=0
while [ $attempt -le 20 ]; do
    attempt=$(( $attempt + 1 ))
    echo "Waiting for zookeeper to startup (attempt: $attempt)..."
    result=$(docker logs $ZOOKEEPER_CONTAINER_NAME)
    if grep -q 'Created server' <<< $result ; then
      echo "Zookeeper is up!"
      break
    fi
    sleep 2
done

### Kafka
RUNNING=$(docker inspect --format="{{ .State.Running }}" $KAFKA_CONTAINER_NAME 2> /dev/null)
if [ "$RUNNING" == "true" ];
  then
      echo "Kafka is already up and running"
  else
    echo "Running kafka..."

    docker run --name $KAFKA_CONTAINER_NAME \
     --network $DOCKER_NETWORK \
     -e KAFKA_BROKER_ID=1 \
     -e KAFKA_ZOOKEEPER_CONNECT=$ZOOKEEPER_CONTAINER_NAME:2181 \
     -e KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://$KAFKA_CONTAINER_NAME:9092,PLAINTEXT_HOST://localhost:29092 \
     -e KAFKA_LISTENER_SECURITY_PROTOCOL_MAP=PLAINTEXT:PLAINTEXT,PLAINTEXT_HOST:PLAINTEXT \
     -e KAFKA_INTER_BROKER_LISTENER_NAME=PLAINTEXT \
     -e KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR=1 \
     -d -p 29092:29092 \
     $KAFKA_IMAGE
fi

echo "Wait for kafka to start with retry for 40 seconds"

attempt=0
while [ $attempt -le 20 ]; do
    attempt=$(( $attempt + 1 ))
    echo "Waiting for kafka to startup (attempt: $attempt)..."
    result=$(docker logs $KAFKA_CONTAINER_NAME)
    if grep -q 'started' <<< "$result" ; then
      echo "kafka is up!"
      break
    fi
    sleep 2
done