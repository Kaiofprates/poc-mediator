package br.kaiofprates.poc_mediator.infrastructure.listenners;

import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import br.kaiofprates.poc_mediator.domain.event.PedidoCriadoEvent;

@Component
public class AuditoriaListener {

    @EventListener
    @Order(2)
    public void registrarAuditoria(PedidoCriadoEvent event) {
        System.out.println("Auditando criação do pedido ID " + event.getPedido().getId() +
            ", descrição: " + event.getPedido().getDescricao());
    }
}
