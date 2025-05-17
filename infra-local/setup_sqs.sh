#!/bin/bash

# Configuração do AWS CLI para LocalStack
export AWS_ACCESS_KEY_ID=test
export AWS_SECRET_ACCESS_KEY=test
export AWS_DEFAULT_REGION=us-east-1
export ENDPOINT_URL=http://localhost:4566

echo "Criando DLQ..."
DLQ_URL=$(aws --endpoint-url=$ENDPOINT_URL sqs create-queue \
    --queue-name pedidos-dlq.fifo \
    --attributes '{
        "FifoQueue": "true",
        "ContentBasedDeduplication": "true"
    }' \
    --query 'QueueUrl' \
    --output text)

echo "Obtendo ARN da DLQ..."
DLQ_ARN=$(aws --endpoint-url=$ENDPOINT_URL sqs get-queue-attributes \
    --queue-url $DLQ_URL \
    --attribute-names QueueArn \
    --query 'Attributes.QueueArn' \
    --output text)

echo "Criando fila principal..."
QUEUE_URL=$(aws --endpoint-url=$ENDPOINT_URL sqs create-queue \
    --queue-name pedidos.fifo \
    --attributes "{
        \"FifoQueue\": \"true\",
        \"ContentBasedDeduplication\": \"true\",
        \"RedrivePolicy\": \"{\\\"deadLetterTargetArn\\\":\\\"$DLQ_ARN\\\",\\\"maxReceiveCount\\\":\\\"3\\\"}\"
    }" \
    --query 'QueueUrl' \
    --output text)

echo "Fila criada: $QUEUE_URL"

echo "Enviando mensagem de teste..."
MESSAGE_ID=$(aws --endpoint-url=$ENDPOINT_URL sqs send-message \
    --queue-url $QUEUE_URL \
    --message-body '{"descricao": "tenis adidas, cor branca, numero 37"}' \
    --message-group-id pedidos \
    --message-deduplication-id $(date +%s) \
    --query 'MessageId' \
    --output text)

echo "Mensagem enviada: $MESSAGE_ID" 