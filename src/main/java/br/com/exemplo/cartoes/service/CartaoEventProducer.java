package br.com.exemplo.cartoes.service;

import br.com.exemplo.cartoes.config.RabbitMqProperties;
import br.com.exemplo.cartoes.domain.event.CartaoAtivadoEvent;
import br.com.exemplo.cartoes.domain.event.CartaoCanceladoEvent;
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
        rabbitTemplate.convertAndSend(properties.exchange(), properties.criadoRoutingKey(), event);
    }

    public void publicarCartaoAtivado(CartaoAtivadoEvent event) {
        rabbitTemplate.convertAndSend(properties.exchange(), properties.ativadoRoutingKey(), event);
    }

    public void publicarCartaoCancelado(CartaoCanceladoEvent event) {
        rabbitTemplate.convertAndSend(properties.exchange(), properties.canceladoRoutingKey(), event);
    }
}