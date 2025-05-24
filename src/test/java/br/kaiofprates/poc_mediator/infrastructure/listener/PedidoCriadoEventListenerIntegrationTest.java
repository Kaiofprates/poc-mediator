package br.kaiofprates.poc_mediator.infrastructure.listener;

import br.kaiofprates.poc_mediator.domain.event.PedidoCriadoEvent;
import br.kaiofprates.poc_mediator.domain.model.Pedido;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
class PedidoCriadoEventListenerIntegrationTest {

    @Autowired
    private PedidoCriadoEventListener listener;

    @BeforeEach
    void setUp() {
        listener.resetExecutionCount();
        listener.setShouldFail(true);
    }

    @Test
    void quandoProcessarEventoComFalha_deveTentarRetry() {
        // Arrange
        Pedido pedido = new Pedido();
        PedidoCriadoEvent event = new PedidoCriadoEvent(this, pedido);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            listener.onApplicationEvent(event);
        });

        assertEquals(3, listener.getExecutionCount());
    }
} 