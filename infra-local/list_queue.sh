#!/bin/bash

# Configuração do AWS CLI para LocalStack
export AWS_ACCESS_KEY_ID=test
export AWS_SECRET_ACCESS_KEY=test
export AWS_DEFAULT_REGION=us-east-1
export ENDPOINT_URL=http://localhost:4566

echo "listando filas"
DLQ_URL=$(aws --endpoint-url=$ENDPOINT_URL sqs list-queues)

echo $DLQ_URL
