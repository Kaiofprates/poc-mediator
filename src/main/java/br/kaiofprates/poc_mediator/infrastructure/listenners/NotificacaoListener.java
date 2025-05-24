package br.kaiofprates.poc_mediator.infrastructure.listenners;

import br.kaiofprates.poc_mediator.domain.event.PedidoCriadoEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class NotificacaoListener extends BaseRetryListener<PedidoCriadoEvent> {

    @Value("${simular.erro.notificacao:false}")
    private boolean simularErro;

    @EventListener
    public void aoCriarPedido(PedidoCriadoEvent event) {
        log.info("Iniciando processamento de notificação para pedido: {}", event.getPedido().getId());
        
        executeWithRetry("notificacaoListener", () -> {
            try {
                processarNotificacao(event);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void processarNotificacao(PedidoCriadoEvent event) throws IOException {
        log.info("Processando notificação para o pedido: {}", event.getPedido().getId());
        
        if (simularErro) {
            log.warn("Simulando erro de IO na notificação - isso irá disparar o mecanismo de retry");
            throw new IOException("Erro simulado ao enviar notificação");
        }
        
        log.info("Notificação enviada com sucesso para o pedido: {}", event.getPedido().getId());
    }
}
