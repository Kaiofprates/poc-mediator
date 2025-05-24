package br.kaiofprates.poc_mediator.infrastructure.sqs;

import br.kaiofprates.poc_mediator.application.usecase.CriarPedidoUseCase;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.util.List;

@Component
public class SqsConsumer {
    private static final Logger logger = LoggerFactory.getLogger(SqsConsumer.class);
    private final CriarPedidoUseCase criarPedidoUseCase;

    @Value("${aws.sqs.enabled:true}")
    private boolean sqsEnabled;

    @Value("${aws.sqs.endpoint}")
    private String sqsEndpoint;

    @Value("${aws.sqs.queue-name}")
    private String queueName;

    private AmazonSQS sqsClient;
    private String queueUrl;

    public SqsConsumer(CriarPedidoUseCase criarPedidoUseCase) {
        this.criarPedidoUseCase = criarPedidoUseCase;
    }

    @PostConstruct
    public void init() {
        if (!sqsEnabled) {
            logger.info("SQS Consumer está desabilitado");
            return;
        }

        BasicAWSCredentials credentials = new BasicAWSCredentials("test", "test");
        
        sqsClient = AmazonSQSClientBuilder.standard()
                .withEndpointConfiguration(new com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration(
                        sqsEndpoint, "us-east-1"))
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();

        try {
            queueUrl = sqsClient.getQueueUrl(queueName).getQueueUrl();
            logger.info("Conectado à fila SQS: {}", queueUrl);
            startConsumer();
        } catch (QueueDoesNotExistException e) {
            logger.error("Fila SQS não encontrada: {}", queueName);
        }
    }

    private void startConsumer() {
        new Thread(() -> {
            while (true) {
                try {
                    ReceiveMessageRequest receiveRequest = new ReceiveMessageRequest()
                            .withQueueUrl(queueUrl)
                            .withMaxNumberOfMessages(10)
                            .withWaitTimeSeconds(20);

                    List<Message> messages = sqsClient.receiveMessage(receiveRequest).getMessages();

                    for (Message message : messages) {
                        try {
                            processMessage(message);
                            deleteMessage(message);
                        } catch (Exception e) {
                            logger.error("Erro ao processar mensagem: {}", message.getMessageId(), e);
                        }
                    }
                } catch (Exception e) {
                    logger.error("Erro ao consumir mensagens da fila", e);
                }
            }
        }).start();
    }

    private void processMessage(Message message) {
        logger.info("Processando mensagem: {}", message.getMessageId());
        PedidoMessage pedidoMessage = new PedidoMessage();
        pedidoMessage.setDescricao(message.getBody());
        logger.info("Pedido recebido: {}", pedidoMessage.getDescricao());
        criarPedidoUseCase.executar(pedidoMessage.getDescricao());
    }

    private void deleteMessage(Message message) {
        sqsClient.deleteMessage(new DeleteMessageRequest()
                .withQueueUrl(queueUrl)
                .withReceiptHandle(message.getReceiptHandle()));
        logger.info("Mensagem deletada: {}", message.getMessageId());
    }
} 