package br.kaiofprates.poc_mediator.infrastructure.listenners;

import br.kaiofprates.poc_mediator.domain.event.PedidoCriadoEvent;
import br.kaiofprates.poc_mediator.domain.model.Pedido;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = {
    "simular.erro.notificacao=true"
})
class NotificacaoListenerIntegrationTest {

    @Autowired
    private NotificacaoListener listener;

    @Test
    void quandoSimularErro_deveTentarRetry() {
        // Arrange
        Pedido pedido = new Pedido();
        PedidoCriadoEvent event = new PedidoCriadoEvent(this, pedido);

        // Act & Assert
        assertThrows(IOException.class, () -> {
            listener.aoCriarPedido(event);
        });
    }
} 