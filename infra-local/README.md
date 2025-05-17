# Infraestrutura Local com LocalStack

Este diretório contém a configuração para executar um ambiente local de AWS usando LocalStack, com foco em SQS.

## Pré-requisitos

- Docker
- Docker Compose
- AWS CLI instalado e configurado

## Configuração

1. Inicie o LocalStack:
```bash
docker-compose up -d
```

2. Aguarde alguns segundos para o LocalStack iniciar completamente.

3. Torne o script executável:
```bash
chmod +x setup_sqs.sh
```

4. Execute o script de configuração:
```bash
./setup_sqs.sh
```

## Estrutura

- `pedidos.fifo`: Fila principal FIFO
- `pedidos-dlq.fifo`: Dead Letter Queue (DLQ) para mensagens que falharam após 3 tentativas

## Enviando Mensagens

O script `setup_sqs.sh` já inclui um exemplo de envio de mensagem. Para enviar novas mensagens, você pode usar o comando AWS CLI:

```bash
aws --endpoint-url=http://localhost:4566 sqs send-message \
    --queue-url <URL_DA_FILA> \
    --message-body '{"descricao": "tenis adidas, cor branca, numero 37"}' \
    --message-group-id pedidos \
    --message-deduplication-id $(date +%s)
```

## Observações

- O LocalStack está configurado para rodar na porta 4566
- As credenciais AWS são fictícias (test/test)
- A região padrão é us-east-1 