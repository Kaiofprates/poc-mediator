package br.kaiofprates.poc_mediator.domain.service;

import br.kaiofprates.poc_mediator.domain.model.Pedido;
import br.kaiofprates.poc_mediator.domain.port.PedidoRepository;
import br.kaiofprates.poc_mediator.domain.event.PedidoCriadoEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class PedidoService {
    
    private final PedidoRepository pedidoRepository;
    private final ApplicationEventPublisher eventPublisher;

    public PedidoService(PedidoRepository pedidoRepository, ApplicationEventPublisher eventPublisher) {
        this.pedidoRepository = pedidoRepository;
        this.eventPublisher = eventPublisher;
    }

    public Pedido criarPedido(String descricao) {
        Pedido pedido = new Pedido(descricao);
        pedido = pedidoRepository.salvar(pedido);
        eventPublisher.publishEvent(new PedidoCriadoEvent(this, pedido));
        return pedido;
    }

    public Pedido buscarPedidoPorId(UUID id) {
        return pedidoRepository.buscarPorId(id);
    }
} 