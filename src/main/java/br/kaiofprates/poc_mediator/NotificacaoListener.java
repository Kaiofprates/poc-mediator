package br.kaiofprates.poc_mediator;

import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
public class NotificacaoListener {

    @EventListener
    @Order(1)
    public void aoCriarPedido(PedidoCriadoEvent event) {
        System.out.println("Enviando e-mail para " + event.emailCliente());
    }
}
