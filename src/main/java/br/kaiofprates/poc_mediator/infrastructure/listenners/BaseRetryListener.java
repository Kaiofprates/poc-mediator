package br.kaiofprates.poc_mediator.infrastructure.listenners;

import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;

@Slf4j
public abstract class BaseRetryListener<T extends ApplicationEvent> {

    @Autowired
    private RetryRegistry retryRegistry;

    protected void executeWithRetry(String retryName, Runnable action) {
        Retry retry = retryRegistry.retry(retryName);
        
        // Adiciona listener para eventos de retry
        retry.getEventPublisher()
            .onRetry(event -> log.info("Tentativa {} de {} para {}", 
                event.getNumberOfRetryAttempts(), 
                retry.getRetryConfig().getMaxAttempts(),
                retryName));

        try {
            retry.executeRunnable(action);
        } catch (Throwable e) {
            // Simplifica o log da exceção
            log.error("Falha após todas as tentativas de retry para {}: {}", 
                retryName, 
                e.getMessage());
            
            if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            }
            throw new RuntimeException(e);
        }
    }
} 