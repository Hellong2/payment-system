#!/bin/bash

FLAG_FILE="/var/lib/kafka/data/.topics_created"\

KAFKA_DATA_DIR="/var/lib/kafka/data"
KAFKA_CLUSTER_ID="tMqzFkVDRhW1sbZ4x9CdbA"  # Generate a unique ID using uuidgen or another method

# Initialize metadata directory if not already initialized
if [ ! -f "$KAFKA_DATA_DIR/meta.properties" ]; then
    echo "Initializing Kafka metadata directory..."
    /opt/kafka/bin/kafka-storage.sh format -t $KAFKA_CLUSTER_ID -c /opt/kafka/config/kraft/server.properties
fi

/opt/kafka/bin/kafka-server-start.sh /opt/kafka/config/kraft/server.properties &

echo "Waiting for Kafka to start..."
sleep 20

if [ ! -f "$FLAG_FILE" ]; then
  echo "Creating topics for the first time..."
  /opt/kafka/bin/kafka-topics.sh --bootstrap-server kafka:9092 --create --topic completed-transactions --partitions 1 --replication-factor 1
  /opt/kafka/bin/kafka-topics.sh --bootstrap-server kafka:9092 --create --topic expired-transactions --partitions 1 --replication-factor 1

  # Write flag file to signal that topics were created.
  touch "$FLAG_FILE"
else
  echo "Topics already created. Skipping topic creation."
fi

wait