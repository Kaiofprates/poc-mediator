package br.kaiofprates.poc_mediator;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class PedidoService {

    private final ApplicationEventPublisher publisher;

    public PedidoService(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    public void criarPedido(Long pedidoId, String emailCliente, Double valor) {
        // 1. Salvar no banco (exemplo omitido)
        System.out.println("Pedido " + pedidoId + " salvo com sucesso.");

        // 2. Publicar evento
        PedidoCriadoEvent evento = new PedidoCriadoEvent(pedidoId, emailCliente, valor);
        publisher.publishEvent(evento);
    }
}
