package br.kaiofprates.poc_mediator;

import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
public class AuditoriaListener {

    @EventListener
    @Order(2)
    public void registrarAuditoria(PedidoCriadoEvent event) {
        System.out.println("Auditando criação do pedido ID " + event.pedidoId() +
            ", valor: " + event.valor());
    }
}
