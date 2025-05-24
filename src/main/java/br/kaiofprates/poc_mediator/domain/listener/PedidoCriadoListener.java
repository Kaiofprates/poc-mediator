package br.kaiofprates.poc_mediator.domain.listener;

import br.kaiofprates.poc_mediator.domain.event.PedidoCriadoEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class PedidoCriadoListener {

    @EventListener
    public void handlePedidoCriadoEvent(PedidoCriadoEvent event) {
        System.out.println("Pedido criado: " + event.getPedido().getId());
    }
} 