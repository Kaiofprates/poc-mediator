package br.kaiofprates.poc_mediator.application.usecase;

import br.kaiofprates.poc_mediator.domain.model.Pedido;
import br.kaiofprates.poc_mediator.domain.service.PedidoService;
import org.springframework.stereotype.Service;

@Service
public class CriarPedidoUseCase {
    
    private final PedidoService pedidoService;

    public CriarPedidoUseCase(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    public Pedido executar(String descricao) {
        return pedidoService.criarPedido(descricao);
    }
} 