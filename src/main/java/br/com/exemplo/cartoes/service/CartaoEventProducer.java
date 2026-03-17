package br.com.exemplo.cartoes.service;

import br.com.exemplo.cartoes.config.RabbitMqProperties;
import br.com.exemplo.cartoes.domain.event.CartaoCriadoEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartaoEventProducer {

    private final RabbitTemplate rabbitTemplate;
    private final RabbitMqProperties properties;

    public void publicarCartaoCriado(CartaoCriadoEvent event) {
        rabbitTemplate.convertAndSend(properties.exchange(), properties.routingKey(), event);
    }
}