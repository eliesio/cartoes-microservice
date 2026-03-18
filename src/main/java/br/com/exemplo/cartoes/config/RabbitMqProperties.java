package br.com.exemplo.cartoes.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.rabbitmq")
public record RabbitMqProperties(
        String exchange,
        String criadoQueue,
        String ativadoQueue,
        String canceladoQueue,
        String criadoRoutingKey,
        String ativadoRoutingKey,
        String canceladoRoutingKey
) {
}