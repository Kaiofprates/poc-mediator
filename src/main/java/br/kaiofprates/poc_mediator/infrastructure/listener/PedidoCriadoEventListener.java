package br.kaiofprates.poc_mediator.infrastructure.listener;

import br.kaiofprates.poc_mediator.domain.event.PedidoCriadoEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
public class PedidoCriadoEventListener extends BaseEventListener<PedidoCriadoEvent> {

    private final AtomicInteger executionCount = new AtomicInteger(0);
    private boolean shouldFail = false;

    @Override
    protected void handleEvent(PedidoCriadoEvent event) throws Exception {
        log.info("Processando evento de pedido criado: {}", event.getPedido().getId());
        executionCount.incrementAndGet();
        
        if (shouldFail) {
            throw new RuntimeException("Erro simulado para testar retry");
        }
    }

    public int getExecutionCount() {
        return executionCount.get();
    }

    public void resetExecutionCount() {
        executionCount.set(0);
    }

    public void setShouldFail(boolean shouldFail) {
        this.shouldFail = shouldFail;
    }
} 