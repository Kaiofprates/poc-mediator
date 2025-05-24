package br.kaiofprates.poc_mediator.infrastructure.persistence;

import br.kaiofprates.poc_mediator.domain.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface SpringDataPedidoRepository extends JpaRepository<Pedido, UUID> {
} 