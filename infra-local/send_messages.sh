#!/bin/bash

# Configuração do AWS CLI para LocalStack
export AWS_ACCESS_KEY_ID=test
export AWS_SECRET_ACCESS_KEY=test
export AWS_DEFAULT_REGION=us-east-1
export ENDPOINT_URL=http://localhost:4566

echo "Enviando mensagem de teste..."
MESSAGE_ID=$(aws --endpoint-url=$ENDPOINT_URL sqs send-message \
    --queue-url "http://sqs.us-east-1.localhost.localstack.cloud:4566/000000000000/pedidos.fifo" \
    --message-body '{"descricao": "tenis adidas, cor branca, numero 37"}' \
    --message-group-id pedidos \
    --message-deduplication-id $(date +%s) \
    --query 'MessageId' \
    --output text)

echo "Mensagem enviada: $MESSAGE_ID" 
