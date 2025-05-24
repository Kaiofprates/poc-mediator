package br.kaiofprates.poc_mediator.infrastructure.persistence;

import br.kaiofprates.poc_mediator.domain.model.Pedido;
import br.kaiofprates.poc_mediator.domain.port.PedidoRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public class JpaPedidoRepository implements PedidoRepository {
    
    private final SpringDataPedidoRepository repository;

    public JpaPedidoRepository(SpringDataPedidoRepository repository) {
        this.repository = repository;
    }

    @Override
    public Pedido salvar(Pedido pedido) {
        return repository.save(pedido);
    }

    @Override
    public Pedido buscarPorId(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
    }
} 