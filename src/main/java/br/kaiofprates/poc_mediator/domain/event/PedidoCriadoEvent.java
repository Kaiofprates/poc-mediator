package br.kaiofprates.poc_mediator.domain.event;

import br.kaiofprates.poc_mediator.domain.model.Pedido;
import org.springframework.context.ApplicationEvent;

public class PedidoCriadoEvent extends ApplicationEvent {
    private final Pedido pedido;

    public PedidoCriadoEvent(Object source, Pedido pedido) {
        super(source);
        this.pedido = pedido;
    }

    public Pedido getPedido() {
        return pedido;
    }
} 