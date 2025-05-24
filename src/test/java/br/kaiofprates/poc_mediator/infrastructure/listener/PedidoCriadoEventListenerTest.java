package br.kaiofprates.poc_mediator.infrastructure.listener;

import br.kaiofprates.poc_mediator.domain.event.PedidoCriadoEvent;
import br.kaiofprates.poc_mediator.domain.model.Pedido;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoCriadoEventListenerTest {

    @Mock
    private RetryRegistry retryRegistry;

    @InjectMocks
    private PedidoCriadoEventListener listener;

    private Retry retry;

    @BeforeEach
    void setUp() throws Throwable {
        retry = mock(Retry.class);
        when(retryRegistry.retry("pedidoListener")).thenReturn(retry);
        doAnswer(invocation -> {
            invocation.getArgument(0, Runnable.class).run();
            return null;
        }).when(retry).executeCheckedSupplier(any());
        listener.resetExecutionCount();
        listener.setShouldFail(false);
    }

    @Test
    void quandoProcessarEventoComSucesso_deveExecutarUmaVez() throws Exception {
        // Arrange
        Pedido pedido = new Pedido();
        PedidoCriadoEvent event = new PedidoCriadoEvent(this, pedido);

        // Act
        listener.onApplicationEvent(event);

        // Assert
        assertEquals(1, listener.getExecutionCount());
    }

    @Test
    void quandoProcessarEventoComFalha_deveTentarRetry() throws Throwable {
        // Arrange
        Pedido pedido = new Pedido();
        PedidoCriadoEvent event = new PedidoCriadoEvent(this, pedido);
        listener.setShouldFail(true);
        doAnswer(invocation -> {
            try {
                invocation.getArgument(0, Runnable.class).run();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return null;
        }).when(retry).executeCheckedSupplier(any());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            listener.onApplicationEvent(event);
        });

        assertEquals(3, listener.getExecutionCount());
    }
} 