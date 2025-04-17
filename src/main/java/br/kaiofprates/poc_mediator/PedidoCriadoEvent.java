package br.kaiofprates.poc_mediator;

public record PedidoCriadoEvent(Long pedidoId, String emailCliente, Double valor) {
}
