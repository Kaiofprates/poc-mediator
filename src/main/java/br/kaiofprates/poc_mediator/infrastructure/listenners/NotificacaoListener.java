package br.kaiofprates.poc_mediator.infrastructure.listenners;

import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import br.kaiofprates.poc_mediator.domain.event.PedidoCriadoEvent;

@Component
public class NotificacaoListener {

    @EventListener
    @Order(1)
    public void aoCriarPedido(PedidoCriadoEvent event) {
        System.out.println("Enviando e-mail para " + event.getPedido().getId());
    }
}
