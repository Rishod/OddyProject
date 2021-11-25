#!/bin/bash

# Variables
IMAGE=postgres:12.5
CONTAINER_NAME=test-pg
PASSWORD=test-pg
USER=test-pg
DB=test
PORT=5432

# If docker has already started, just go ahead and keep running.
echo "Running start-pg.sh from $PWD"

RUNNING=$(docker inspect --format="{{ .State.Running }}" $CONTAINER_NAME 2> /dev/null)
if [ "$RUNNING" == "true" ]; then
    echo "Postgres is already up and running"
    exit 0
fi

# Run a postgres image with the name "postgres", attach the
docker run --name $CONTAINER_NAME \
 -e POSTGRES_PASSWORD=$PASSWORD \
 -e POSTGRES_USER=$USER \
 -e POSTGRES_DB=$DB \
 -d -p $PORT:5432 \
 $IMAGE


# Wait for postgres to load before starting ddl
echo "Wait for postgres to start with retry for 40 seconds"

attempt=0
while [ $attempt -le 20 ]; do
    attempt=$(( $attempt + 1 ))
    echo "Waiting for postgres to startup (attempt: $attempt)..."
    result=$(docker logs $CONTAINER_NAME)
    if grep -q 'PostgreSQL init process complete; ready for start up.' <<< $result ; then
      echo "postgres is up!"
      break
    fi
    sleep 2
done
