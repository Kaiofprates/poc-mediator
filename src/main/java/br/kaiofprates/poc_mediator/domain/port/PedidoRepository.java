package br.kaiofprates.poc_mediator.domain.port;

import br.kaiofprates.poc_mediator.domain.model.Pedido;
import java.util.UUID;

public interface PedidoRepository {
    Pedido salvar(Pedido pedido);
    Pedido buscarPorId(UUID id);
} 