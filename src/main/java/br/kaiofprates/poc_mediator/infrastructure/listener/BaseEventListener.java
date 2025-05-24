package br.kaiofprates.poc_mediator.infrastructure.listener;

import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;

@Slf4j
public abstract class BaseEventListener<T extends ApplicationEvent> {

    @Autowired
    private RetryRegistry retryRegistry;

    public void onApplicationEvent(T event) {
        Retry retry = retryRegistry.retry("pedidoListener");
        try {
            retry.executeCheckedSupplier(() -> {
                handleEvent(event);
                return null;
            });
        } catch (Throwable e) {
            log.error("Erro ao processar evento: {}", e.getMessage(), e);
            if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            }
            throw new RuntimeException(e);
        }
    }

    protected abstract void handleEvent(T event) throws Exception;
} 