#!/bin/bash

# Configuração do AWS CLI para LocalStack
export AWS_ACCESS_KEY_ID=test
export AWS_SECRET_ACCESS_KEY=test
export AWS_DEFAULT_REGION=us-east-1
export ENDPOINT_URL=http://localhost:4566

# Nome da DLQ (ajuste conforme necessário)
DLQ_NAME="pedidos-dlq.fifo"

echo "Listando mensagens da DLQ: $DLQ_NAME"

# Obtém a URL da DLQ
DLQ_URL=$(aws --endpoint-url=$ENDPOINT_URL sqs get-queue-url --queue-name $DLQ_NAME --query 'QueueUrl' --output text)

if [ -z "$DLQ_URL" ]; then
    echo "Erro: Não foi possível encontrar a DLQ com o nome $DLQ_NAME"
    exit 1
fi

echo "URL da DLQ: $DLQ_URL"

# Recebe até 10 mensagens da DLQ
aws --endpoint-url=$ENDPOINT_URL sqs receive-message \
    --queue-url $DLQ_URL \
    --max-number-of-messages 10 \
    --attribute-names All \
    --message-attribute-names All \
    --query 'Messages[*].{Body:Body,MessageId:MessageId,Attributes:Attributes}' \
    --output json 